package ro.unibuc.fmi.karate_auth_service.dtos.athlete;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Gender;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete}
 */
@Schema(description = "Athlete response")
public record AthleteResponse(Long id, Set<Role> roles, String lastName, String firstName, String email,
                              @NotNull(message = "Nationality cannot be null") @Size(message = "Nationality should be between 2 and 100 characters", min = 2, max = 100) String nationality,
                              @NotNull(message = "Birth date cannot be null") LocalDate birthDate,
                              @NotNull(message = "Gender cannot be null") Gender gender,
                              @Min(message = "Height must be at least 50 cm", value = 50) @Max(message = "Height must be no greater than 250 cm", value = 250) Integer height,
                              @Min(message = "Weight must be at least 20 kg", value = 20) @Max(message = "Weight must be no greater than 200 kg", value = 200) Integer weight) implements Serializable {
}