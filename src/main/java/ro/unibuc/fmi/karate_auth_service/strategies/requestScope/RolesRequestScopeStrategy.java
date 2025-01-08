package ro.unibuc.fmi.karate_auth_service.strategies.requestScope;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestWithRolesApproval;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class RolesRequestScopeStrategy implements RequestScopeStrategy {
    private final RequestScope requestScope = RequestScope.ROLES;

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
}
