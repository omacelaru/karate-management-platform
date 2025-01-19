package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

public interface RequestTypeStrategy {
    RequestType getRequestType();

    Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable);

    Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable);

    RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status);

    RequestInfoResponseInterface createRequest(User user, Object request);

    RequestInfoResponseInterface editRequest(User user, Object request);

    RequestInfoResponseInterface revokeRequest(User user, Long requestId);

    RequestInfoResponseInterface activateRequest(User user, Long requestId);

    boolean isRequestType(Long requestId);
}
