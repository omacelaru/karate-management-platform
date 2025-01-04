package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestInfoRepositoryFactory requestInfoRepositoryFactory;
    private final MapperUtils mapperUtils;

    @Transactional
    public CoachCreationResponse createCoachCreationRequest(User user, @Valid CoachRequest coachRequest) {
        var coachCreationRequestRepository = (CoachCreationRequestRepository) requestInfoRepositoryFactory.getRepository(CoachCreationRequestRepository.class);

        if (user.getRoles().contains(Role.COACH)) {
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
}
