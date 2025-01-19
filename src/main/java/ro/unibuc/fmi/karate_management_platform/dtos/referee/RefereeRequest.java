package ro.unibuc.fmi.karate_management_platform.dtos.referee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ro.unibuc.fmi.karate_management_platform.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_management_platform.models.referee.RefereeLevel;

import java.io.Serializable;

/**
 * DTO for a referee creation request.
 * This class represents the structure required to send a request for creating a referee,
 * including the relevant details about the referee's license and refereeLevel.
 *
 * @param licenseInfo Information about the referee's license (series and number).
 * @param refereeLevel       The refereeLevel of the referee (A, B, C, D).
 */
@Schema(name = "RefereeRequest", description = "DTO representing the request for creating a referee. It includes necessary information about the referee's license and refereeLevel.")
public record RefereeRequest(@Valid
                             @NotNull
                             LicenseInfo licenseInfo,

                             @NotNull
                             @Enumerated(EnumType.STRING)
                             RefereeLevel refereeLevel
) implements Serializable {
}