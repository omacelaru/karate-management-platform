package ro.unibuc.fmi.karate_auth_service.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.referee.RefereeCategory;
import ro.unibuc.fmi.karate_auth_service.models.request.referee.RefereeCreationRequest;

import java.io.Serializable;


/**
 * Data Transfer Object (DTO) for {@link RefereeCreationRequest}.
 * This DTO represents the response structure when a referee creation request is processed.
 * It contains the details of the referee creation request, including the request metadata,
 * roles associated with the referee, relevant licensing information, and the referee category.
 * Fields:
 * - request: The request metadata and roles associated with the referee.
 * - licenseInfo: Information about the referee's license (series and number).
 * - category: The category of the referee (A, B, C, D).
 * <p>Usage: This DTO is used for returning the response for a referee creation request from the backend to the client.</p>
 * <p>Relations: This class is related to the {@link RequestWithRolesApprovalResponse}, {@link LicenseInfo}, and {@link RefereeCategory} classes.</p>
 *
 * @see RequestWithRolesApprovalResponse
 * @see LicenseInfo
 * @see RefereeCategory
 */
@Schema(name = "RefereeCreationResponse", description = "DTO representing the response for a referee creation request.")
@Getter
@Setter
public final class RefereeCreationResponse extends RequestWithRolesApprovalResponse implements Serializable {
    LicenseInfo licenseInfo;
    RefereeCategory category;
}
