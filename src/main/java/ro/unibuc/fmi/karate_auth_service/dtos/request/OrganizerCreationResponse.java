package ro.unibuc.fmi.karate_auth_service.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_auth_service.models.request.organizer.OrganizerCreationRequest;


/**
 * DTO representing the response for an organizer creation request.
 * This DTO is used for returning the response for an organizer creation request from the backend to the client.
 * It contains the details of the organizer creation request, including the request metadata and roles associated with the organizer.
 * Fields:
 * - request: The request metadata and roles associated with the organizer.
 * <p>Usage: This DTO is used for returning the response for an organizer creation request from the backend to the client.</p>
 * <p>Relations: This class is related to the {@link RequestWithRolesApprovalResponse} class.</p>
 *
 * @see RequestWithRolesApprovalResponse
 * @see OrganizerCreationRequest
 */
@Schema(name = "OrganizerCreationResponse", description = "DTO representing the response for an organizer creation request.")
@Getter
@Setter
public final class OrganizerCreationResponse extends RequestWithRolesApprovalResponse {
    @Embedded
    private OrganizerRequest organizerRequest;
}
