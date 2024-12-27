package ro.unibuc.fmi.karate_auth_service.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link ro.unibuc.fmi.karate_auth_service.models.user.User}
 */
@Schema(description = "User name request")
public record UserRequestName(@NotBlank
                              @Length(max = 60, message = "Last name should be at most 60 characters")
                              String lastName,

                              @NotBlank
                              @Length(max = 120, message = "First name should be at most 120 characters")
                              String firstName
) implements Serializable {
}