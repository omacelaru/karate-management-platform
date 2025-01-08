package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.util.Set;

public interface RequestTypeStrategy {
    RequestType getRequestType();

    Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable);

    Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable);

    RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status);

    RequestInfoResponseInterface createRequest(User user, Object request, Set<?> approvers);
}
