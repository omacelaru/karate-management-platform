package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestWithRolesApproval;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface RequestWithRolesApprovalRepository<T extends RequestWithRolesApproval> extends RequestInfoRepository<T> {
    Page<? extends RequestInfo> findAllByApproverRolesInAndStatusIn(Set<Role> roles, Set<RequestStatus> activeStatuses, Pageable pageable);

    Optional<T> findByIdAndApproverRolesInAndStatusIn(Long requestId, Set<Role> roles, Set<RequestStatus> activeStatuses);
}
