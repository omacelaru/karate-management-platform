package ro.unibuc.fmi.karate_auth_service.repositories;

import ro.unibuc.fmi.karate_auth_service.models.request.athlete.AthleteCreationRequest;

public interface AthleteCreationRequestRepository extends RequestWithUsersApprovalRepository<AthleteCreationRequest> {
}