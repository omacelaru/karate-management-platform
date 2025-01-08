package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.factories.RequestTypeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestTypeStrategyFactory strategyFactory;
    private final RequestInfoRepositoryFactory requestInfoRepositoryFactory;
    private final MapperUtils mapperUtils;


    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable, RequestType requestType) {
        log.info("Getting requests made by user with email: {}", user.getEmail());
        return strategyFactory.getStrategy(requestType).getRequestsMadeByMe(user, pageable);
    }

    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable, RequestType requestType) {
        log.info("Getting requests assigned to user with email: {}", user.getEmail());
        return strategyFactory.getStrategy(requestType).getRequestsAssignedToMe(user, pageable);
    }

    @Transactional
    public RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestType requestType, RequestStatus status) {
        log.info("Updating request with id: {} to status: {}", requestId, status);
        return strategyFactory.getStrategy(requestType).updateRequestStatus(user, requestId, status);
    }

//    @Transactional
//    public CoachCreationResponse createCoachCreationRequest(User user, @Valid CoachRequest coachRequest) {
//        var coachCreationRequestRepository = requestInfoRepositoryFactory.getRepository(RequestType.COACH_CREATION);
//
//        if (user.getRoles().contains(Role.COACH)) {
//            log.error("A coach already exists for user with email: {}", user.getEmail());
//            throw new IllegalStateException("You already have a coach account. If you need to make changes, you can edit your current account.");
//        }
//        if (coachCreationRequestRepository.existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING))) {
//            log.error("A pending coach creation request already exists for user with email: {}", user.getEmail());
//            //todo set location where to edit the request
//            throw new IllegalStateException("You already have a pending coach creation request. If you need to make changes, you can edit your current request.");
//        }
//
//        log.info("Creating coach creation request for user with email: {}", user.getEmail());
//
//        CoachCreationRequest coachCreationRequest = mapperUtils.mapToCoachCreationRequest(coachRequest);
//        coachCreationRequest.setApproverRoles(Set.of(Role.ADMIN));
//        coachCreationRequest.setCreatedBy(user);
//        coachCreationRequest.setLastUpdatedById(user.getId());
//
//        coachCreationRequestRepository.save(coachCreationRequest);
//
//        return mapperUtils.mapToCoachCreationResponse(coachCreationRequest);
//    }
//
//
//    @Transactional
//    public RefereeCreationResponse createRefereeCreationRequest(User user, @Valid RefereeRequest refereeRequest) {
//        var refereeCreationRequestRepository = requestInfoRepositoryFactory.getRepository(RequestType.REFEREE_CREATION);
//
//        if (user.getRoles().contains(Role.REFEREE)) {
//            log.error("A referee already exists for user with email: {}", user.getEmail());
//            throw new IllegalStateException("You already have a referee account. If you need to make changes, you can edit your current account.");
//        }
//        if (refereeCreationRequestRepository.existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING))) {
//            log.error("A pending referee creation request already exists for user with email: {}", user.getEmail());
//            //todo set location where to edit the request
//            throw new IllegalStateException("You already have a pending referee creation request. If you need to make changes, you can edit your current request.");
//        }
//
//        log.info("Creating referee creation request for user with email: {}", user.getEmail());
//
//        RefereeCreationRequest refereeCreationRequest = mapperUtils.mapToRefereeCreationRequest(refereeRequest);
//        refereeCreationRequest.setApproverRoles(Set.of(Role.ADMIN));
//        refereeCreationRequest.setCreatedBy(user);
//        refereeCreationRequest.setLastUpdatedById(user.getId());
//
//        refereeCreationRequestRepository.save(refereeCreationRequest);
//
//        return mapperUtils.mapToRefereeCreationResponse(refereeCreationRequest);
//    }

    @Transactional
    public RequestInfoResponseInterface createCoachCreationRequest(User user, @Valid CoachRequest coachRequest) {
        log.info("Creating coach creation request for user with email: {}", user.getEmail());
        return strategyFactory.getStrategy(RequestType.COACH_CREATION).createRequest(user, coachRequest, Set.of(Role.ADMIN));
    }

    @Transactional
    public RequestInfoResponseInterface createRefereeCreationRequest(User user, @Valid RefereeRequest refereeRequest) {
        log.info("Creating referee creation request for user with email: {}", user.getEmail());
        return strategyFactory.getStrategy(RequestType.REFEREE_CREATION).createRequest(user, refereeRequest, Set.of(Role.ADMIN));
    }


}
