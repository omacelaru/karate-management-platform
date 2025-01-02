package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository athleteRepository;
    private final MapperUtils mapperUtils;

    public AthleteResponse getMe(User user) throws IncompleteProfileException {
        String email = user.getEmail();

        log.info("Getting athlete with email: {}", email);
        Athlete athlete = athleteRepository.findByUserEmail(email).orElseThrow(IncompleteProfileException::new);

        return mapperUtils.mapToAthleteResponse(athlete);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AthleteResponse createAthlete(User user, AthleteRequest athleteRequest) {
        UserService.applyRolesToUser(user, Role.ATHLETE);

        Athlete athlete = mapperUtils.mapToAthlete(athleteRequest);
        athlete.setUser(user);

        Athlete athleteSaved = athleteRepository.save(athlete);
        return mapperUtils.mapToAthleteResponse(athleteSaved);
    }

}
