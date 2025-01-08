package ro.unibuc.fmi.karate_auth_service.strategies.requestType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
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

    public abstract RequestType getRequestType();

    @SuppressWarnings("unchecked")
    protected <T extends RequestInfo> RequestInfoRepository<T> getRepository() {
        return repositoryFactory.getRepository(getRequestType());
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsMadeByMe(User user, Pageable pageable) {
        return getRepository().findAllByCreatedById(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<? extends RequestInfoResponseInterface> getRequestsAssignedToMe(User user, Pageable pageable) {
        return getRepository().findAllByApproverRolesInAndStatusIn(
                user.getRoles(),
                Set.of(RequestStatus.PENDING, RequestStatus.IN_PROGRESS, RequestStatus.PARTIALLY_COMPLETED),
                pageable
        ).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface updateRequestStatus(User user, Long requestId, RequestStatus status) {
        RequestInfo request = getRepository().findByIdAndApproverRolesIn(requestId, user.getRoles())
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        validateRequest(request, status);

        request.setStatus(status);
        request.setLastUpdatedById(user.getId());

        RequestInfo updatedRequest = getRepository().save(request);

        if (status == RequestStatus.ACCEPTED) {
            handleAcceptedRequest(user, updatedRequest);
        }

        return mapToResponse(updatedRequest);
    }

    @Override
    @Transactional
    public RequestInfoResponseInterface createRequest(User user, Object request, Set<?> approvers) {
        if (isDuplicateRequest(user)) {
            log.error("Duplicate request {}", request);
            throw new IllegalStateException("Duplicate request");
        }

        RequestInfo newRequest = mapToEntity(user, request);

        RequestScopeStrategy strategy = scopeStrategyFactory.getStrategy(newRequest.getScope());
        strategy.setApprovers(newRequest, approvers);

        newRequest.setCreatedBy(user);
        newRequest.setLastUpdatedById(user.getId());

        return mapToResponse(getRepository().save(newRequest));
    }

    protected boolean isDuplicateRequest(User user) {
        return getRepository().existsByCreatedByIdAndStatusIn(user.getId(), Set.of(RequestStatus.PENDING));
    }

    protected void validateRequest(RequestInfo request, RequestStatus status) {
        if (status != RequestStatus.ACCEPTED && status != RequestStatus.REJECTED) {
            log.error("Invalid status: {} for request with id: {}. Valid statuses are: ACCEPTED, REJECTED", status, request.getId());
            throw new IllegalArgumentException("Invalid status: " + status + " for request with id: " + request.getId());
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            log.error("Request with id: {} is not pending", request.getId());
            throw new IllegalArgumentException("Request is not pending");
        }
        if (request.getType() != getRequestType()) {
            throw new IllegalArgumentException("Request type does not match");
        }
    }

    protected abstract void handleAcceptedRequest(User user, RequestInfo request);

    protected abstract RequestInfo mapToEntity(User user, Object request);

    protected abstract RequestInfoResponseInterface mapToResponse(RequestInfo request);
}

