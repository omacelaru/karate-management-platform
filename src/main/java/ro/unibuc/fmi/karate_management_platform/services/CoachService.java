package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.CoachRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoachService {
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final MapperUtils mapperUtils;

    public Page<CoachResponse> getAllCoaches(Pageable pageable) {
        log.info("Getting all coaches");
        return coachRepository.findAll(pageable).map(mapperUtils::mapToCoachResponse);
    }

    public CoachResponse getMe(User user) throws IncompleteProfileException {
        String email = user.getEmail();

        log.info("Getting coach with email: {}", email);
        Coach coach = coachRepository.findByUserEmail(email).orElseThrow(IncompleteProfileException::new);
        
        // Get teams for the coach
        var teams = teamRepository.findByAthletes_Coaches_User_Email(email);
        
        return mapperUtils.mapToCoachResponse(coach, teams);
    }

    @Transactional
    public Coach createCoach(User user, @Valid CoachRequest coachRequest) {
        log.info("Creating coach for user with email: {}", user.getEmail());
        user.addRole(Role.COACH);

        Coach coach = mapperUtils.mapToCoach(coachRequest);
        coach.setUser(user);

        return coachRepository.save(coach);
    }

    public Coach findCoachByEmail(String email) {
        log.info("Finding coach by email: {}", email);
        return coachRepository.findByUserEmail(email).orElseThrow(() -> {
            log.error("Coach with email {} not found", email);
            return new IllegalArgumentException("Coach with email " + email + " not found");
        });
    }
}
