package ro.unibuc.fmi.karate_auth_service.dtos.referee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;

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
                             @Pattern(regexp = "^[A-D]$", message = "Category must be one of A, B, C, D")
                             String category
) implements Serializable {
}