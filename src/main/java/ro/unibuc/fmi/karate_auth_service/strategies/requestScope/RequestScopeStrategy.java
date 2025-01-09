package ro.unibuc.fmi.karate_auth_service.strategies.requestScope;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestScope;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.RequestInfoRepository;

import java.util.Optional;
import java.util.Set;

public interface RequestScopeStrategy {
    RequestScope getRequestScope();

    void setApprovers(RequestInfo request, Set<?> approvers);

    <T extends RequestInfo> Page<? extends RequestInfo> getRequestsAssignedToMe(User user, Pageable pageable, RequestInfoRepository<T> repository);

    Optional<? extends RequestInfo> getRequestAssignedToMeById(Long requestId, User user, Pageable unpaged, RequestInfoRepository<? extends RequestInfo> repository);
}
