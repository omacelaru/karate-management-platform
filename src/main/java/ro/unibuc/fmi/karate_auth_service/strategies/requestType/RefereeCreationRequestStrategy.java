package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.referee.RefereeCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.RefereeCreationRequestRepository;
import ro.unibuc.fmi.karate_auth_service.repositories.UserRepository;
import ro.unibuc.fmi.karate_auth_service.services.RefereeService;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Component
@Getter
@RequiredArgsConstructor
public class RefereeCreationRequestStrategy implements RequestTypeStrategy {
    private final RequestType requestType = RequestType.REFEREE_CREATION;
    private final RefereeCreationRequestRepository refereeCreationRequestRepository;
    private final RefereeService refereeService;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        return refereeCreationRequestRepository.findAllByCreatedById(user.getId(), pageable).map(mapperUtils::mapToRefereeCreationResponse);
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable) {
        return refereeCreationRequestRepository.findAllByApproverRolesInAndStatusIn(user.getRoles(),
                Set.of(RequestStatus.PENDING, RequestStatus.IN_PROGRESS, RequestStatus.PARTIALLY_COMPLETED),
                pageable).map(mapperUtils::mapToRefereeCreationResponse);
    }

    @Override
    public RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status) {
        RefereeCreationRequest refereeCreationRequest = refereeCreationRequestRepository.findByIdAndApproverRolesIn(requestId, user.getRoles())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (refereeCreationRequest.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }
        if (refereeCreationRequest.getType() != requestType) {
            throw new IllegalArgumentException("Request type does not match");
        }

        refereeCreationRequest.setStatus(status);
        refereeCreationRequest.setLastUpdatedById(user.getId());

        RefereeCreationRequest updatedRequest = refereeCreationRequestRepository.save(refereeCreationRequest);
        if (status == RequestStatus.ACCEPTED) {
            //refereeService.createReferee(refereeCreationRequest);
        }

        return mapperUtils.mapToRefereeCreationResponse(updatedRequest);
    }

    @Override
    public RequestInfoResponseInterface createRequest(User user, Object request) {
        if (!(request instanceof RefereeRequest refereeRequest)) {
            throw new IllegalArgumentException("Invalid request type");
        }

        RefereeCreationRequest creationRequest = mapperUtils.mapToRefereeCreationRequest(refereeRequest);
        creationRequest.setCreatedBy(user);
        creationRequest.setStatus(RequestStatus.PENDING);

        RefereeCreationRequest savedRequest = refereeCreationRequestRepository.save(creationRequest);
        return mapperUtils.mapToRefereeCreationResponse(savedRequest);
    }

}
