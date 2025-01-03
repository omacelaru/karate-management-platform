package ro.unibuc.fmi.karate_auth_service.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.request.coach.CoachCreationRequest;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.io.Serializable;
import java.util.Set;

/**
 * Data Transfer Object (DTO) for {@link CoachCreationRequest}.
 * This DTO represents the response structure when a coach creation request is processed.
 * It contains the details of the coach creation request, including the request metadata,
 * roles associated with the coach, and relevant licensing information.
 * Fields:
 * - {@code id}: Unique identifier for the coach creation request.
 * - {@code type}: The type of the request, indicating what kind of action is being performed.
 * - {@code status}: The current status of the request, indicating whether it is pending, approved, or rejected.
 * - {@code createdById}: The identifier of the user who initiated the coach creation request.
 * - {@code lastUpdatedById}: The identifier of the user who last updated the coach creation request.
 * - {@code note}: Optional additional notes or comments related to the coach creation request.
 * - {@code roles}: A set of roles assigned to the coach being created (e.g., "Head Coach", "Assistant Coach").
 * - {@code licenseInfo}: Licensing information associated with the coach, including details such as their certification or qualifications.
 * This DTO is used for returning the processed data from a coach creation operation to the client, ensuring all relevant
 * information is included in the response.
 *
 * @see CoachCreationRequest
 */
@Schema(name = "CoachCreationResponse", description = "DTO representing the response for a coach creation request.")
public record CoachCreationResponse(
        Long id,
        RequestType type,
        RequestStatus status,
        Long createdById,
        Long lastUpdatedById,
        String note,
        Set<Role> roles,
        LicenseInfo licenseInfo
) implements Serializable {
}