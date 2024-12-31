package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;
import ro.unibuc.fmi.karate_auth_service.utils.UserUtils;

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

    public CoachResponse getMe() throws IncompleteProfileException {
        User user = UserUtils.getCurrentUser();
        String email = user.getEmail();
        log.info("Getting coach with email: {}", email);
        return coachRepository.findByUserEmail(email).map(mapperUtils::mapToCoachResponse).orElseThrow(IncompleteProfileException::new);
    }

    @Transactional
    public CoachResponse createCoach(@Valid CoachRequest coachRequest) {
        User user = UserUtils.getCurrentUser();
        UserService.applyRolesToUser(user, Role.COACH);

        Coach coach = mapperUtils.mapToCoach(coachRequest);
        coach.setUser(user);

        Coach coachSaved = coachRepository.save(coach);
        return mapperUtils.mapToCoachResponse(coachSaved);

    }
}
