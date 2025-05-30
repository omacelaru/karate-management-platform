package ro.unibuc.fmi.karate_management_platform.strategies.requestScope;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestScope;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithUsersApproval;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.request.RequestInfoRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.request.RequestWithUsersApprovalRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Component
public class UserRequestScopeStrategy implements RequestScopeStrategy {
    private final RequestScope requestScope = RequestScope.USERS;

    @SuppressWarnings("unchecked")
    private <T extends RequestInfo> RequestWithUsersApprovalRepository<RequestWithUsersApproval> validateRepository(RequestInfoRepository<T> repository) {
        if (repository instanceof RequestWithUsersApprovalRepository<?>) {
            return (RequestWithUsersApprovalRepository<RequestWithUsersApproval>) repository;
        } else {
            log.error("Repository is not of type RequestWithUsersApprovalRepository");
            throw new IllegalArgumentException("Repository is not of type RequestWithUsersApprovalRepository");
        }
    }

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

    @Override
    public <T extends RequestInfo> Page<? extends RequestInfo> getRequestsAssignedToMe(User user, Pageable pageable, RequestInfoRepository<T> repository) {
        RequestWithUsersApprovalRepository<RequestWithUsersApproval> validRepository = validateRepository(repository);
        return validRepository.findAllByApproverUsersContaining(user, pageable);
    }

    @Override
    public Optional<? extends RequestInfo> getRequestAssignedToMeById(Long requestId, User user, Pageable pageable, RequestInfoRepository<? extends RequestInfo> repository) {
        RequestWithUsersApprovalRepository<RequestWithUsersApproval> validRepository = validateRepository(repository);
        return validRepository.findByIdAndApproverUsersContaining(requestId, Set.of(user));
    }
}
