package ro.unibuc.fmi.karate_management_platform.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for {@link AthleteCreationResponse}.
 * This DTO represents the response structure when an athlete creation request is processed.
 * It contains the details of the athlete creation request, including the request metadata.
 * Fields:
 * - request: The request metadata associated with the athlete.
 * <p>Usage: This DTO is used for returning the response for an athlete creation request from the backend to the client.</p>
 * <p>Relations: This class is related to the {@link RequestWithUsersApprovalResponse} class.</p>
 *
 * @see RequestWithUsersApprovalResponse
 * @see AthleteRequest
 */
@Schema
@Getter
@Setter
public final class AthleteCreationResponse extends RequestWithUsersApprovalResponse implements Serializable {
    @Embedded
    private AthleteRequest athleteRequest;
}
