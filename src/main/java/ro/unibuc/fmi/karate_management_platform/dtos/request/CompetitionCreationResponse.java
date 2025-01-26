package ro.unibuc.fmi.karate_management_platform.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for {@link CompetitionCreationResponse}.
 * This DTO represents the response structure when a competition creation request is processed.
 * It contains the details of the competition creation request, including the request metadata.
 * Fields:
 * - request: The request metadata associated with the competition.
 * <p>Usage: This DTO is used for returning the response for a competition creation request from the backend to the client.</p>
 * <p>Relations: This class is related to the {@link RequestWithRolesApprovalResponse} class.</p>
 *
 * @see RequestWithRolesApprovalResponse
 * @see CompetitionRequest
 */
@Schema(name = "CompetitionCreationResponse", description = "DTO representing the response for a competition creation request.")
@Getter
@Setter
public final class CompetitionCreationResponse extends RequestWithRolesApprovalResponse implements Serializable {
    @Embedded
    private CompetitionRequest competitionRequest;
}
