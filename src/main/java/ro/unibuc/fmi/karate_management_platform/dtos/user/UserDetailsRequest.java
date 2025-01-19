package ro.unibuc.fmi.karate_management_platform.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for user details request for {@link ro.unibuc.fmi.karate_management_platform.models.user.User}.
 * This DTO is used to send the user's personal details when creating or updating a user profile.
 *
 * <p>Usage: This DTO is typically used for user registration or profile update requests.</p>
 */
@Schema(description = "User details request containing the user's personal information.")

public record UserDetailsRequest(@NotBlank
                                 @Length(max = 60, message = "Last name should be at most 60 characters")
                                 String lastName,

                                 @NotBlank
                                 @Length(max = 120, message = "First name should be at most 120 characters")
                                 String firstName,

                                 @NotNull(message = "Nationality cannot be null")
                                 @Size(message = "Nationality should be between 2 and 100 characters", min = 2, max = 100)
                                 @NotBlank
                                 String nationality,

                                 @NotNull(message = "Birth date cannot be null")
                                 @Past
                                 LocalDate birthDate,

                                 @NotNull(message = "Gender cannot be null")
                                 Gender gender,

                                 @URL
                                 String profilePictureUrl
) implements Serializable {
}