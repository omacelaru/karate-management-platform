package ro.unibuc.fmi.karate_management_platform.dtos.organizer;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.embedded.TRN.TaxRegistrationNumber;

import java.io.Serializable;

/**
 * DTO for an organizer response.
 * This class represents the structure of the response for an organizer creation request.
 * <p>Usage: This DTO is used for returning organizer data from the backend to the client.</p>
 *
 * @param taxRegistrationNumber
 */
@Schema(name = "OrganizerResponse", description = "DTO representing the response for an organizer. It includes necessary information about the organizer's tax registration number.")
public record OrganizerResponse(
        UserResponse user,
        TaxRegistrationNumber taxRegistrationNumber
) implements Serializable {
}