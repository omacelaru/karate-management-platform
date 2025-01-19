package ro.unibuc.fmi.karate_management_platform.strategies.requestScope;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithRolesApproval;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.request.RequestInfoRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.request.RequestWithRolesApprovalRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus.ACTIVE_STATUSES;

@Slf4j
@Getter
@Component
public class RolesRequestScopeStrategy implements RequestScopeStrategy {
    private final RequestScope requestScope = RequestScope.ROLES;

    @SuppressWarnings("unchecked")
    private <T extends RequestInfo> RequestWithRolesApprovalRepository<RequestWithRolesApproval> validateRepository(RequestInfoRepository<T> repository) {
        if (repository instanceof RequestWithRolesApprovalRepository<?>) {
            return (RequestWithRolesApprovalRepository<RequestWithRolesApproval>) repository;
        } else {
            log.error("Repository is not of type RequestWithRolesApprovalRepository");
            throw new IllegalArgumentException("Repository is not of type RequestWithRolesApprovalRepository");
        }
    }

    @Override
    public void setApprovers(RequestInfo request, Set<?> approvers) {
        Set<Role> roles = approvers.stream()
                .filter(Role.class::isInstance)
                .map(Role.class::cast)
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            log.error("No valid roles provided for approvers");
            throw new IllegalArgumentException("No valid roles provided for approvers");
        }

        RequestWithRolesApproval requestWithRolesApproval = (RequestWithRolesApproval) request;
        requestWithRolesApproval.setApproverRoles(Set.of(Role.ADMIN));
    }

    @Override
    public <T extends RequestInfo> Page<? extends RequestInfo> getRequestsAssignedToMe(User user, Pageable pageable, RequestInfoRepository<T> repository) {
        RequestWithRolesApprovalRepository<RequestWithRolesApproval> validRepository = validateRepository(repository);
        return validRepository.findAllByApproverRolesInAndStatusIn(user.getRoles(), ACTIVE_STATUSES, pageable);
    }

    @Override
    public Optional<? extends RequestInfo> getRequestAssignedToMeById(Long requestId, User user, Pageable unpaged, RequestInfoRepository<? extends RequestInfo> repository) {
        RequestWithRolesApprovalRepository<RequestWithRolesApproval> validRepository = validateRepository(repository);
        return validRepository.findByIdAndApproverRolesInAndStatusIn(requestId, user.getRoles(), ACTIVE_STATUSES);
    }
}
