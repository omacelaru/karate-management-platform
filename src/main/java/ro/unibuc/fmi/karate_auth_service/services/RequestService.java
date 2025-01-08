package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.factories.RequestTypeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestTypeStrategyFactory strategyFactory;

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

    @Transactional
    public RequestInfoResponseInterface createRequest(User user, RequestType requestType, Object request, Set<?> approvers) {
        log.info("Creating request for user with email: {}", user.getEmail());
        return strategyFactory.getStrategy(requestType).createRequest(user, request, approvers);
    }

    @Transactional
    public RequestInfoResponseInterface editRequest(User user, RequestType requestType, Long requestId, Object request) {
        log.info("Editing request with id: {}", requestId);
        return strategyFactory.getStrategy(requestType).editRequest(user, requestId, request);
    }

    @Transactional
    public RequestInfoResponseInterface deleteRequest(User user, RequestType requestType, Long requestId) {
        log.info("Deleting request with id: {}", requestId);
        return strategyFactory.getStrategy(requestType).deleteRequest(user, requestId);
    }
}
