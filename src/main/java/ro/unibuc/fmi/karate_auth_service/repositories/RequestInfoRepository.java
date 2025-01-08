package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface RequestInfoRepository<T extends RequestInfo> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);

    Page<T> findAllByCreatedById(@NonNull Long createdById, Pageable pageable);

    Page<T> findAllByApproverRolesInAndStatusIn(Set<?> approverRoles, Collection<RequestStatus> statuses, Pageable pageable);

    Optional<T> findByIdAndApproverRolesInAndStatusInAndType(Long id, Set<?> approverRoles, Collection<RequestStatus> statuses, RequestType type);

    Optional<T> findByIdAndCreatedByIdAndStatusInAndType(Long id, Long createdById, Collection<RequestStatus> statuses, RequestType type);
}