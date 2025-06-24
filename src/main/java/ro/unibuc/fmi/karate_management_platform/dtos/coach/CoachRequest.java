package ro.unibuc.fmi.karate_management_platform.dtos.coach;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;

import java.io.Serializable;

/**
 * DTO for a coach creation request.
 * This class represents the structure required to send a request for creating a coach,
 * including the relevant details about the coach's license.
 *
 * @param licenseInfo Information about the coach's license (series and number).
 */
@Embeddable
@Schema(name = "CoachRequest", description = "DTO representing the request for creating a coach. It includes necessary information about the coach's license.")
public record CoachRequest(
        @Schema(description = "Information about the coach's license, which includes the series and number of the license.")
        @Valid
        LicenseInfo licenseInfo
) implements Serializable {
}