package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface CoachCreationRequestRepository extends RequestInfoRepository<CoachCreationRequest> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);

    Page<CoachCreationRequest> findAllByCreatedById(@NonNull Long createdById, Pageable pageable);

    Page<CoachCreationRequest> findAllByApproverRolesIn(Set<?> approverRoles, Pageable pageable);

    Optional<CoachCreationRequest> findByIdAndApproverRolesIn(Long id, Set<?> approverRoles);


}