package ro.unibuc.fmi.karate_auth_service.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) for {@link CoachCreationRequest}.
 * This DTO represents the response structure when a coach creation request is processed.
 * It contains the details of the coach creation request, including the request metadata,
 * roles associated with the coach, and relevant licensing information.
 * Fields:
 * - request: The request metadata and roles associated with the coach.
 * - licenseInfo: Information about the coach's license (series and number).
 * <p>Usage: This DTO is used for returning the response for a coach creation request from the backend to the client.</p>
 * <p>Relations: This class is related to the {@link RequestWithRolesApprovalResponse} and {@link LicenseInfo} classes.</p>
 *
 * @see RequestWithRolesApprovalResponse
 * @see LicenseInfo
 * @see CoachCreationRequest
 */
@Schema(name = "CoachCreationResponse", description = "DTO representing the response for a coach creation request.")
@Getter
@Setter
public final class CoachCreationResponse extends RequestWithRolesApprovalResponse implements Serializable {
    LicenseInfo licenseInfo;
}