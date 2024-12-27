package ro.unibuc.fmi.karate_auth_service.dtos.athlete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserRequestName;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Gender;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link Athlete}
 */
@Schema(description = "Athlete request")
public record AthleteRequest(
        @NotNull
        UserRequestName userRequestName,

        @NotNull(message = "Nationality cannot be null")
        @Size(message = "Nationality should be between 2 and 100 characters", min = 2, max = 100)
        @NotBlank
        String nationality,

        @NotNull(message = "Birth date cannot be null")
        @Past LocalDate birthDate,
        @NotNull(message = "Gender cannot be null")
        Gender gender,

        @NotNull @Min(message = "Height must be at least 50 cm", value = 50)
        @Max(message = "Height must be no greater than 250 cm", value = 250)
        Integer height,

        @NotNull @Min(message = "Weight must be at least 20 kg", value = 20)
        @Max(message = "Weight must be no greater than 200 kg", value = 200)
        Integer weight,

        @URL String profilePictureUrl
) implements Serializable {
}