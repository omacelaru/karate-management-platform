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
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoachService {
    private final CoachRepository coachRepository;
    private final CoachCreationRequestRepository coachCreationRequestRepository;
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
    public CoachCreationResponse createCoachCreationRequest(User user, @Valid CoachRequest coachRequest) {
        if (coachRepository.existsByUserEmail(user.getEmail())) {
            log.error("A coach already exists for user with email: {}", user.getEmail());
            throw new IllegalStateException("You already have a coach account. If you need to make changes, you can edit your current account.");
        }

        if (coachCreationRequestRepository.existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING))) {
            log.error("A pending coach creation request already exists for user with email: {}", user.getEmail());
            //todo set location where to edit the request
            throw new IllegalStateException("You already have a pending coach creation request. If you need to make changes, you can edit your current request.");
        }

        log.info("Creating coach creation request for user with email: {}", user.getEmail());

        CoachCreationRequest coachCreationRequest = mapperUtils.mapToCoachCreationRequest(coachRequest);
        coachCreationRequest.setApproverRoles(Set.of(Role.ADMIN));
        coachCreationRequest.setCreatedById(user.getId());
        coachCreationRequest.setLastUpdatedById(user.getId());

        coachCreationRequestRepository.save(coachCreationRequest);

        return mapperUtils.mapToCoachCreationResponse(coachCreationRequest);
    }

    @Transactional
    public CoachResponse createCoach(User user, @Valid CoachRequest coachRequest) {
        UserService.applyRolesToUser(user, Role.COACH);

        Coach coach = mapperUtils.mapToCoach(coachRequest);
        coach.setUser(user);

        Coach coachSaved = coachRepository.save(coach);
        return mapperUtils.mapToCoachResponse(coachSaved);

    }
}
