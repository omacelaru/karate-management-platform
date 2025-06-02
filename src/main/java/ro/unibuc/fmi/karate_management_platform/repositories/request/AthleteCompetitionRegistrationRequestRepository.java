package ro.unibuc.fmi.karate_management_platform.repositories.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.AthleteCompetitionRegistrationRequest;

public interface AthleteCompetitionRegistrationRequestRepository extends RequestWithUsersApprovalRepository<AthleteCompetitionRegistrationRequest> {
    boolean existsByCreatedBy_IdAndCompetitionId(Long id, Long competitionId);

}