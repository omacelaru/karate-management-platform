package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.CoachRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final AthleteRepository athleteRepository;
    private final MapperUtils mapperUtils;
    private final CoachRepository coachRepository;

    public boolean existsByMembersIds(Set<Long> athletesIds) {
        log.info("Checking if team exists by members ids: {}", athletesIds);
        return teamRepository.existsByAthletes_IdIn(athletesIds);
    }

    public TeamResponse createTeam(User user, TeamRequest teamRequest) {
        log.info("Creating team with name: {} for coach: {}", teamRequest.getTeamName(), user.getUsername());

        validateTeamNameUnique(teamRequest.getTeamName());
        List<Athlete> athletes = validateAndGetAthletes(teamRequest.getAthleteIds());
        Coach coach = validateAndGetCoach(user.getEmail());
        validateAthletesBelongToCoach(coach, teamRequest.getAthleteIds());
        validateNoDuplicateTeam(teamRequest.getAthleteIds());
        validateGenderConsistency(athletes);
        validateAgeGroupConsistency(athletes);

        Team team = Team.builder()
                .teamName(teamRequest.getTeamName())
                .athletes(new HashSet<>(athletes))
                .build();

        Team savedTeam = teamRepository.save(team);
        return mapperUtils.mapToTeamResponse(savedTeam);
    }


    private void validateTeamNameUnique(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            log.error("Team with name {} already exists", teamName);
            throw new IllegalArgumentException("Team with name " + teamName + " already exists");
        }
    }

    private List<Athlete> validateAndGetAthletes(Set<Long> athleteIds) {
        List<Athlete> athletes = athleteRepository.findAllById(athleteIds);
        if (athletes.size() != athleteIds.size()) {
            log.error("One or more athletes not found for IDs: {}", athleteIds);
            throw new IllegalArgumentException("One or more athletes not found");
        }
        return athletes;
    }

    private Coach validateAndGetCoach(String email) {
        return coachRepository.findByUserEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Coach not found for user: " + email));
    }

    private void validateAthletesBelongToCoach(Coach coach, Set<Long> athleteIds) {
        Set<Long> coachAthleteIds = coach.getAthletes().stream()
                .map(Athlete::getId)
                .collect(Collectors.toSet());
        if (!coachAthleteIds.containsAll(athleteIds)) {
            log.warn("Not all athletes belong to the coach: {}", coach.getUser().getUsername());
            throw new IllegalArgumentException("Not all athletes belong to the coach: " + coach.getUser().getUsername());
        }
    }

    private void validateNoDuplicateTeam(Set<Long> athleteIds) {
        Set<Long> newIds = new HashSet<>(athleteIds);
        List<Team> possibleTeams = teamRepository.findAll();
        for (Team existingTeam : possibleTeams) {
            Set<Long> existingIds = existingTeam.getAthletes().stream()
                    .map(Athlete::getId)
                    .collect(Collectors.toSet());
            if (existingIds.equals(newIds)) {
                log.error("A team with the same athletes already exists: {}", existingIds);
                throw new IllegalArgumentException("A team with the same athletes already exists");
            }
        }
    }

    private void validateGenderConsistency(List<Athlete> athletes) {
        Set<Gender> genders = athletes.stream()
                .map(athlete -> athlete.getUser().getGender())
                .collect(Collectors.toSet());
        if (genders.size() > 1) {
            log.error("Athletes must be of the same gender. Found genders: {}", genders);
            throw new IllegalArgumentException("All athletes must be of the same gender");
        }
    }

    private void validateAgeGroupConsistency(List<Athlete> athletes) {
        Set<AgeGroup> ageGroups = athletes.stream()
                .map(athlete -> AgeGroup.getAgeGroupByAge(
                        Period.between(athlete.getUser().getBirthDate(), LocalDate.now()).getYears()))
                .collect(Collectors.toSet());
        if (ageGroups.size() > 1) {
            log.error("Athletes must be in the same age group. Found age groups: {}", ageGroups);
            throw new IllegalArgumentException("All athletes must be in the same age group");
        }
    }

    public Team findTeamByMembersIds(Set<Long> athletesIds) {
        log.info("Finding team by members ids: {}", athletesIds);
        return teamRepository.findByAthletes_IdIn(athletesIds).orElseThrow(() -> {
            log.error("Team with members ids {} not found", athletesIds);
            return new IllegalArgumentException("Team with members ids " + athletesIds + " not found");
        });
    }

    public Team getTeamById(Long id) {
        log.info("Getting team by ID {}", id);
        return teamRepository.findById(id).orElseThrow(() -> {
            log.error("Team with ID {} not found", id);
            return new IllegalArgumentException("Team with ID " + id + " not found");
        });
    }

    public AgeGroup getAgeGroup(Team team, LocalDate date) {
        log.info("Getting age group for team {} on date {}", team.getId(), date);

        Map<AgeGroup, Long> groupCounts = team.getAthletes().stream()
                .map(athlete -> {
                    int age = Period.between(athlete.getUser().getBirthDate(), date).getYears();
                    return AgeGroup.getAgeGroupByAge(age);
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (groupCounts.size() == 1) {
            return groupCounts.keySet().iterator().next();
        } else if (groupCounts.size() == 2) {
            Map.Entry<AgeGroup, Long> major = groupCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow();
            Map.Entry<AgeGroup, Long> minor = groupCounts.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals(major.getKey()))
                    .findFirst()
                    .orElseThrow();

            if (minor.getValue() == 1) {
                return major.getKey();
            }
        }

        throw new IllegalArgumentException("Team " + team.getId() + " has inconsistent age groups: " + groupCounts);
    }


    public Gender getGender(Team team) {
        log.info("Getting gender for team {}", team.getId());
        Set<Gender> genders = team.getAthletes().stream()
                .map(athlete -> athlete.getUser().getGender())
                .collect(Collectors.toSet());

        return switch (genders.size()) {
            case 1 -> genders.iterator().next();
            default -> Gender.OTHER;
        };
    }

    public List<TeamResponse> getAllTeams(User user) {
        log.info("Getting all teams for coach: {}", user.getUsername());
        Coach coach = validateAndGetCoach(user.getEmail());

        // Get all teams that have at least one athlete from this coach
        Set<Team> teams = teamRepository.findByAthletes_Coaches_User_Email(coach.getUser().getEmail());
        if (teams.isEmpty()) {
            log.warn("No teams found for coach: {}", coach.getUser().getUsername());
            return Collections.emptyList();
        }

        return teams.stream()
                .map(mapperUtils::mapToTeamResponse)
                .collect(Collectors.toList());
    }
}
