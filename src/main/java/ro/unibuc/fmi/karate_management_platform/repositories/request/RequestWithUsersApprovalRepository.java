package ro.unibuc.fmi.karate_management_platform.repositories.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestWithUsersApproval;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface RequestWithUsersApprovalRepository<T extends RequestWithUsersApproval> extends RequestInfoRepository<T> {
    Page<? extends RequestInfo> findAllByApproverUsersContaining(User user, Pageable pageable);

    Optional<? extends RequestInfo> findByIdAndApproverUsersContaining(Long requestId, Set<User> user);
}