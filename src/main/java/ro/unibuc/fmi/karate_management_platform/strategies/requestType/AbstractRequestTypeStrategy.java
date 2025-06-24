package ro.unibuc.fmi.karate_management_platform.strategies.requestType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.exceptions.RequestNotFoundException;
import ro.unibuc.fmi.karate_management_platform.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_management_platform.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.request.RequestInfoRepository;
import ro.unibuc.fmi.karate_management_platform.strategies.requestScope.RequestScopeStrategy;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Optional;
import java.util.Set;

import static ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus.ACTIVE_STATUSES;


@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractRequestTypeStrategy implements RequestTypeStrategy {
    protected final MapperUtils mapperUtils;
    protected final RequestInfoRepositoryFactory repositoryFactory;
    protected final RequestScopeStrategyFactory scopeStrategyFactory;

    public abstract RequestType getRequestType();

    protected <T extends RequestInfo> RequestInfoRepository<T> getRepository() {
        return repositoryFactory.getRepository(getRequestType());
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        log.info("Getting requests made by user with email: {} and id: {}", user.getEmail(), user.getId());
        Page<? extends RequestInfo> requests = getRepository().findAllByCreatedById(user.getId(), pageable);
        log.debug("Found {} requests for user {}", requests.getTotalElements(), user.getId());
        return requests.map(this::mapToResponse);
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable) {
        log.info("Getting requests assigned to user with email: {}", user.getEmail());
        RequestScopeStrategy strategy = scopeStrategyFactory.getStrategy(getRequestType());
        Page<? extends RequestInfo> requestInfos = strategy.getRequestsAssignedToMe(user, pageable, getRepository());
        return requestInfos.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status) {
        log.info("Updating request with id: {} to status: {}", requestId, status);
        RequestScopeStrategy strategy = scopeStrategyFactory.getStrategy(getRequestType());
        Optional<? extends RequestInfo> optionalRequest = strategy.getRequestAssignedToMeById(requestId, user, Pageable.unpaged(), getRepository());

        RequestInfo request = optionalRequest.orElseThrow(() -> new RequestNotFoundException(requestId, ACTIVE_STATUSES));

        validateRequestStatus(request, status);

        request.setStatus(status);
        request.setLastUpdatedById(user.getId());

        RequestInfo updatedRequest = getRepository().save(request);

        if (status == RequestStatus.ACCEPTED) {
            log.info("Handling accepted request with id: {}", requestId);
            handleAcceptedRequest(user, updatedRequest);
        }

        return mapToResponse(updatedRequest);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface createRequest(User user, Object request) {
        log.info("Creating request for user with email: {}", user.getEmail());

        validateRequest(user, request);

        if (isDuplicateRequest(user, request)) {
            log.error("Duplicate request {}", request);
            throw new IllegalStateException("Duplicate request found. You can edit the existing request");
        }

        RequestInfo newRequest = mapToEntity(user, request);

        processRequestApproval(user, newRequest);

        newRequest.setCreatedBy(user);
        newRequest.setLastUpdatedById(user.getId());

        return mapToResponse(getRepository().save(newRequest));
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface editRequest(User user, Object request) {
        RequestInfo existingRequestToBeUpdated = getRepository().findByCreatedBy_IdAndStatusInAndType(user.getId(), ACTIVE_STATUSES, getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(null, ACTIVE_STATUSES));

        log.info("Editing request ID {} for user with email: {}", existingRequestToBeUpdated.getId(), user.getEmail());
        RequestInfo updatedRequest = mapToEntity(user, request);

        processRequestApproval(user, updatedRequest);

        updateSpecificFields(existingRequestToBeUpdated, updatedRequest);


        return mapToResponse(existingRequestToBeUpdated);
    }

    private void processRequestApproval(User user, RequestInfo request) {
        Set<?> approvers = handleApprovers(user, request);
        RequestScopeStrategy strategy = scopeStrategyFactory.getStrategy(request.getScope());
        strategy.setApprovers(request, approvers);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface revokeRequest(User user, Long requestId) {
        log.info("Deleting request with id: {}", requestId);
        RequestInfo request = getRepository().findByIdAndCreatedByIdAndStatusInAndType(requestId, user.getId(), ACTIVE_STATUSES, getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(requestId, ACTIVE_STATUSES));
        request.setStatus(RequestStatus.REVOKED);
        request.setLastUpdatedById(user.getId());

        return mapToResponse(request);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface activateRequest(User user, Long requestId) {
        log.info("Activating request with id: {}", requestId);
        RequestInfo request = getRepository().findByIdAndCreatedByIdAndStatusInAndType(requestId, user.getId(), Set.of(RequestStatus.REVOKED), getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(requestId, Set.of(RequestStatus.REVOKED)));

        request.setStatus(RequestStatus.PENDING);
        request.setLastUpdatedById(user.getId());

        return mapToResponse(request);
    }

    protected void validateRequestStatus(RequestInfo request, RequestStatus status) {
        if (status != RequestStatus.ACCEPTED && status != RequestStatus.REJECTED) {
            log.error("Invalid status: {} for request with id: {}. Valid statuses are: ACCEPTED, REJECTED", status, request.getId());
            throw new IllegalArgumentException("Invalid status: " + status + " for request with id: " + request.getId());
        }
    }

    public boolean isRequestType(Long requestId) {
        return getRepository().existsById(requestId);
    }

    protected boolean isDuplicateRequest(User user, Object request) {
        return getRepository().existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING));
    }

    protected abstract void validateRequest(User user, Object request);

    protected abstract void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest);

    protected abstract void handleAcceptedRequest(User user, RequestInfo request);

    protected abstract Set<?> handleApprovers(User user, Object request);

    protected abstract RequestInfo mapToEntity(User user, Object request);

    protected abstract RequestInfoResponseInterface mapToResponse(RequestInfo request);
}

