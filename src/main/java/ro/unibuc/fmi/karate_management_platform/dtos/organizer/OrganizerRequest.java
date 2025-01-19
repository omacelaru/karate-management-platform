package ro.unibuc.fmi.karate_management_platform.dtos.organizer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;

import java.io.Serializable;

/**
 *
 * DTO for an organizer creation request.
 * This class represents the structure required to send a request for creating an organizer,
 * including the relevant details about the organizer's tax registration number.
 *
 * @param taxRegistrationNumber
 */
@Schema(name = "OrganizerRequest", description = "DTO representing the request for creating an organizer. It includes necessary information about the organizer's tax registration number.")
public record OrganizerRequest(
        @Valid
        @NotNull TaxRegistrationNumber taxRegistrationNumber
) implements Serializable {
}