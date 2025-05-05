package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final AthleteRepository athleteRepository;
    private final MapperUtils mapperUtils;

    public boolean existsByMembersIds(Set<Long> athletesIds) {
        log.info("Checking if team exists by members ids: {}", athletesIds);
        return teamRepository.existsByAthletes_IdIn(athletesIds);
    }

    public Team createTeam(Set<Long> athletesIds) {
        log.info("Creating team with athletes ids: {}", athletesIds);
        List<Athlete> athletes = athleteRepository.findAllById(athletesIds);
        Set<Athlete> athletesSet = new HashSet<>(athletes);
        Team team = Team.builder()
                .athletes(athletesSet)
                .build();
        return teamRepository.save(team);
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
}
