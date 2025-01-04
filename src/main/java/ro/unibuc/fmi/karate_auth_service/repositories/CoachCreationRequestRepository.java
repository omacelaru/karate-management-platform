package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;

import java.util.Collection;

public interface CoachCreationRequestRepository extends JpaRepository<CoachCreationRequest, Long>, JpaSpecificationExecutor<CoachCreationRequest> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);
}