package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;

import java.util.Collection;

public interface CoachCreationRequestRepository extends RequestInfoRepository<CoachCreationRequest> {
    boolean existsByCreatedByIdAndStatusIn(@NonNull Long createdById, @NonNull Collection<RequestStatus> statuses);
}