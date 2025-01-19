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
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository athleteRepository;
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
    public void createAthlete(User user, AthleteRequest athleteRequest) {
        log.info("Creating athlete for user with email: {}", user.getEmail());
        UserService.applyRolesToUser(user, Role.ATHLETE);

        Athlete athlete = mapperUtils.mapToAthlete(athleteRequest);
        athlete.setUser(user);

        Set<Coach> coaches = clubService.getCoachesForClub(athleteRequest.clubId());
        athlete.setCoaches(coaches);

        athleteRepository.save(athlete);
    }


}
