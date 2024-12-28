package ro.unibuc.fmi.karate_auth_service.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Gender;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for the response containing user details for {@link ro.unibuc.fmi.karate_auth_service.models.user.User}.
 * This DTO is used to send user information after a successful user query or action.
 *
 * <p>Usage: This DTO is typically used in user profile responses or user listing responses.</p>
 */
@Schema(description = "Response containing user details such as ID, roles, name, and email.")
//TODO investigate for id duplication
public record UserResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Set<Role> roles,
                           String lastName, String firstName, String email, String nationality, LocalDate birthDate,
                           Gender gender, String profilePictureUrl) implements Serializable {
}