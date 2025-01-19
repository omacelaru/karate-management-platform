package ro.unibuc.fmi.karate_management_platform.dtos.referee;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_management_platform.models.referee.RefereeLevel;

import java.io.Serializable;

/**
 * DTO for a referee response.
 * This class represents the structure required to send a response for a referee,
 * including the relevant details about the referee's license, refereeLevel, and user details.
 * <p>Usage: This DTO is used for returning referee data from the backend to the client.</p>
 *
 * @param user        User details of the referee.
 * @param licenseInfo Information about the referee's license (series and number).
 * @param refereeLevel    The refereeLevel of the referee (A, B, C, D).
 */
@Schema(name = "RefereeResponse", description = "DTO representing the response for a referee. It includes necessary information about the referee's license, refereeLevel, and user details.")
public record RefereeResponse(
        UserResponse user,
        LicenseInfo licenseInfo,
        RefereeLevel refereeLevel
) implements Serializable {
}