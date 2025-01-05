package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.util.Collection;
import java.util.Set;

public interface CoachCreationRequestRepository extends RequestInfoRepository<CoachCreationRequest> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);

    Page<CoachCreationRequest> findAllByCreatedById(@NonNull Long createdById, Pageable pageable);

    Page<CoachCreationRequest> findAllByApproverRolesIn(Set<Role> approverRoles, Pageable pageable);


}