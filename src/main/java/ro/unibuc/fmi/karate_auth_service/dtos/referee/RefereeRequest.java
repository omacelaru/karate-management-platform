package ro.unibuc.fmi.karate_auth_service.dtos.referee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ro.unibuc.fmi.karate_auth_service.models.embedded.license.LicenseInfo;
import ro.unibuc.fmi.karate_auth_service.models.referee.RefereeCategory;

import java.io.Serializable;

/**
 * DTO for a referee creation request.
 * This class represents the structure required to send a request for creating a referee,
 * including the relevant details about the referee's license and category.
 *
 * @param licenseInfo Information about the referee's license (series and number).
 * @param category    The category of the referee (A, B, C, D).
 */
@Schema(name = "RefereeRequest", description = "DTO representing the request for creating a referee. It includes necessary information about the referee's license and category.")
public record RefereeRequest(@Valid
                             @NotNull
                             LicenseInfo licenseInfo,

                             @NotNull
                             @Enumerated(EnumType.STRING)
                             RefereeCategory category
) implements Serializable {
}