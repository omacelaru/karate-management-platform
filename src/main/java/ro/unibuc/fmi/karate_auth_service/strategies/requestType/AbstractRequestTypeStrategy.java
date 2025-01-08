package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.exceptions.RequestNotFoundException;
import ro.unibuc.fmi.karate_auth_service.factories.RequestInfoRepositoryFactory;
import ro.unibuc.fmi.karate_auth_service.factories.RequestScopeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.RequestInfoRepository;
import ro.unibuc.fmi.karate_auth_service.strategies.requestScope.RequestScopeStrategy;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractRequestTypeStrategy implements RequestTypeStrategy {
    protected final MapperUtils mapperUtils;
    private final RequestInfoRepositoryFactory repositoryFactory;
    private final RequestScopeStrategyFactory scopeStrategyFactory;
    private final Set<RequestStatus> activeStatuses = Set.of(RequestStatus.PENDING, RequestStatus.IN_PROGRESS, RequestStatus.PARTIALLY_COMPLETED);

    public abstract RequestType getRequestType();

    protected <T extends RequestInfo> RequestInfoRepository<T> getRepository() {
        return repositoryFactory.getRepository(getRequestType());
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        log.info("Getting requests made by user with email: {}", user.getEmail());
        return getRepository().findAllByCreatedById(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable) {
        log.info("Getting requests assigned to user with email: {}", user.getEmail());
        return getRepository().findAllByApproverRolesInAndStatusIn(
                user.getRoles(),
                Set.of(RequestStatus.PENDING, RequestStatus.IN_PROGRESS, RequestStatus.PARTIALLY_COMPLETED),
                pageable
        ).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status) {
        log.info("Updating request with id: {} to status: {}", requestId, status);
        //TODO - check if scope is roles or users
        RequestInfo request = getRepository().findByIdAndApproverRolesInAndStatusInAndType(requestId, user.getRoles(), activeStatuses, getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(requestId, activeStatuses));

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
    public RequestInfoResponseInterface createRequest(User user, Object request, Set<?> approvers) {
        log.info("Creating request for user with email: {}", user.getEmail());
        if (isUserAlreadyHasRequestedRole(user)) {
            log.error("User {} already has the requested role", user.getId());
            throw new IllegalStateException("User already has the requested role.");
        }
        if (isDuplicateRequest(user)) {
            log.error("Duplicate request {}", request);
            //todo set location where to edit the request
            throw new IllegalStateException("Duplicate request found. You can edit the existing request");
        }

        RequestInfo newRequest = mapToEntity(user, request);

        RequestScopeStrategy strategy = scopeStrategyFactory.getStrategy(newRequest.getScope());
        strategy.setApprovers(newRequest, approvers);

        newRequest.setCreatedBy(user);
        newRequest.setLastUpdatedById(user.getId());

        return mapToResponse(getRepository().save(newRequest));
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface editRequest(User user, Long requestId, Object request) {
        log.info("Editing request with id: {}", requestId);
        RequestInfo existingRequestToBeUpdated = getRepository().findByIdAndCreatedByIdAndStatusInAndType(requestId, user.getId(), activeStatuses, getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(requestId, activeStatuses));

        RequestInfo updatedRequest = mapToEntity(user, request);

        updateSpecificFields(existingRequestToBeUpdated, updatedRequest);

        return mapToResponse(existingRequestToBeUpdated);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface revokeRequest(User user, Long requestId) {
        log.info("Deleting request with id: {}", requestId);
        RequestInfo request = getRepository().findByIdAndCreatedByIdAndStatusInAndType(requestId, user.getId(), activeStatuses, getRequestType())
                .orElseThrow(() -> new RequestNotFoundException(requestId, activeStatuses));
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

    protected boolean isDuplicateRequest(User user) {
        return getRepository().existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING));
    }

    protected abstract boolean isUserAlreadyHasRequestedRole(User user);

    protected abstract void updateSpecificFields(RequestInfo existingRequest, RequestInfo updatedRequest);

    protected abstract void handleAcceptedRequest(User user, RequestInfo request);

    protected abstract RequestInfo mapToEntity(User user, Object request);

    protected abstract RequestInfoResponseInterface mapToResponse(RequestInfo request);
}

