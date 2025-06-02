package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository athleteRepository;
    private final TeamRepository teamRepository;
    private final MapperUtils mapperUtils;
    private final ClubService clubService;

    public Page<AthleteResponse> getAllAthletes(Pageable pageable) {
        log.info("Getting all athletes");
        return athleteRepository.findAll(pageable).map(mapperUtils::mapToAthleteResponse);
    }

    public AthleteResponse getMe(User user) throws IncompleteProfileException {
        String email = user.getEmail();

        log.info("Getting athlete with email: {}", email);
        Athlete athlete = athleteRepository.findByUserEmail(email).orElseThrow(IncompleteProfileException::new);

        return mapperUtils.mapToAthleteResponse(athlete);
    }

    @Transactional
    public Athlete createAthlete(User user, AthleteRequest athleteRequest) {
        log.info("Creating athlete for user with email: {}", user.getEmail());
        user.addRole(Role.ATHLETE);

        Athlete athlete = mapperUtils.mapToAthlete(athleteRequest);
        athlete.setUser(user);

        Set<Coach> coaches = clubService.getCoachesForClub(athleteRequest.clubId());
        athlete.setCoaches(coaches);

        return athleteRepository.save(athlete);
    }


    public void updateAthlete(Athlete athlete) {
        log.info("Updating athlete with id: {}", athlete.getId());
        athleteRepository.save(athlete);
    }

    public Athlete getAthleteById(Long athleteId) throws IllegalArgumentException {
        log.info("Getting athlete by ID {}", athleteId);
        return athleteRepository.findById(athleteId).orElseThrow(() -> {
            log.error("Athlete with ID {} not found", athleteId);
            return new IllegalArgumentException("Athlete with ID " + athleteId + " not found");
        });
    }

    public AgeGroup getAgeGroup(Athlete athlete, LocalDate competitionStartDate) {
        if (athlete == null || competitionStartDate == null) {
            throw new IllegalArgumentException("Athlete and competition start date must not be null");
        }

        LocalDate birthDate = athlete.getUser().getBirthDate();
        if (birthDate == null) {
            throw new IllegalArgumentException("Athlete's birth date must not be null");
        }

        int age = Period.between(birthDate, competitionStartDate).getYears();

        log.info("Calculating age group for athlete with ID={} (birthDate={}) and competitionStartDate={}. Calculated age: {}",
                athlete.getId(), birthDate, competitionStartDate, age);

        return AgeGroup.getAgeGroupByAge(age);
    }

    public Set<Coach> findCoachesByAthleteId(Long id) {
        log.info("Finding coaches for athlete with ID: {}", id);
        Athlete athlete = getAthleteById(id);
        return athlete.getCoaches();
    }

    public List<Team> getTeamsByAthleteId(Long id) {
        log.info("Finding teams for athlete with ID: {}", id);
        Athlete athlete = getAthleteById(id);
        return teamRepository.findAllByAthletesContains(athlete);
    }
}
