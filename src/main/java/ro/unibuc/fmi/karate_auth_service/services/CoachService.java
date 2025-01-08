package ro.unibuc.fmi.karate_auth_service.services;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoachService {
    private final CoachRepository coachRepository;
    private final MapperUtils mapperUtils;

    public Page<CoachResponse> getAllCoaches(Pageable pageable) {
        log.info("Getting all coaches");
        return coachRepository.findAll(pageable).map(mapperUtils::mapToCoachResponse);
    }

    public CoachResponse getMe(User user) throws IncompleteProfileException {
        String email = user.getEmail();

        log.info("Getting coach with email: {}", email);
        Coach coach = coachRepository.findByUserEmail(email).orElseThrow(IncompleteProfileException::new);

        return mapperUtils.mapToCoachResponse(coach);
    }

    @Transactional
    public void createCoach(User user, @Valid CoachRequest coachRequest) {
        UserService.applyRolesToUser(user, Role.COACH);

        Coach coach = mapperUtils.mapToCoach(coachRequest);
        coach.setUser(user);

        coachRepository.save(coach);
    }
}
