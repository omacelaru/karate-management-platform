package ro.unibuc.fmi.karate_auth_service.strategies.requestScope;

import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;

import java.util.Set;

public interface RequestScopeStrategy {
    RequestScope getRequestScope();

    void setApprovers(RequestInfo request, Set<?> approvers);
}
