package ro.unibuc.fmi.karate_management_platform.dtos.athlete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for the athlete request data, used for creating or updating an athlete's information.
 * This includes personal details such as height and weight.
 * <p>Usage: This DTO is used for passing athlete data from the client to the backend when creating or updating an athlete's profile.</p>
 */
@Embeddable
@Schema(description = "Athlete request containing personal details such as height and weight.")
//TODO all DTO have message for filed with validation rules
public record AthleteRequest(
        @NotNull(message = "Club ID must not be null")
        Long clubId,

        @NotNull
        @Min(message = "Height must be at least 50 cm", value = 50)
        @Max(message = "Height must be no greater than 250 cm", value = 250)
        Integer height,

        @NotNull
        @Min(message = "Weight must be at least 20 kg", value = 20)
        @Max(message = "Weight must be no greater than 200 kg", value = 200)
        Integer weight
) implements Serializable {
}