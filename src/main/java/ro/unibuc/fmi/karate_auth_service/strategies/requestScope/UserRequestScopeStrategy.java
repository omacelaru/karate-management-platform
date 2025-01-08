package ro.unibuc.fmi.karate_auth_service.strategies.requestScope;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestWithUsersApproval;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class UserRequestScopeStrategy implements RequestScopeStrategy {
    private final RequestScope requestScope = RequestScope.USERS;

    @Override
    public void setApprovers(RequestInfo request, Set<?> approvers) {
        Set<User> users = approvers.stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .collect(Collectors.toSet());

        if (users.isEmpty()) {
            log.error("No valid users provided for approvers");
            throw new IllegalArgumentException("No valid users provided for approvers");
        }

        RequestWithUsersApproval requestWithUsersApproval = (RequestWithUsersApproval) request;
        requestWithUsersApproval.setApproverUsers(users);
    }
}
