package ro.unibuc.fmi.karate_management_platform.repositories.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestInfo;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;

import java.util.Collection;
import java.util.Optional;

@NoRepositoryBean
public interface RequestInfoRepository<T extends RequestInfo> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);

    Page<T> findAllByCreatedById(@NonNull Long createdById, Pageable pageable);

    Optional<T> findByIdAndCreatedByIdAndStatusInAndType(Long id, Long createdById, Collection<RequestStatus> statuses, RequestType type);

    Optional<T> findByCreatedBy_IdAndStatusInAndType(Long id, Collection<RequestStatus> statuses, RequestType type);
}