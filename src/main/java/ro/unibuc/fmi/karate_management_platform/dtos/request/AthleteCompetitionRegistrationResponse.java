package ro.unibuc.fmi.karate_management_platform.dtos.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.AthleteCompetitionRegistrationRequest;

import java.io.Serializable;

/**
 * Response DTO for athlete competition registration requests.
 * This class is used to represent the response when an athlete registers for a competition.
 */
@Schema(name = "AthleteCompetitionRegistrationResponse", description = "DTO representing the response for an athlete competition registration request.")
@Getter
@Setter
public final class AthleteCompetitionRegistrationResponse extends RequestWithUsersApprovalResponse implements Serializable {
    @Embedded
    private AthleteCompetitionRegistrationRequest athleteCompetitionRegistrationRequest;
}
