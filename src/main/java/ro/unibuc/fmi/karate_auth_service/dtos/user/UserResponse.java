package ro.unibuc.fmi.karate_auth_service.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link ro.unibuc.fmi.karate_auth_service.models.user.User}
 */
@Schema(description = "User response")
public record UserResponse(Long id, Set<Role> roles, String lastName, String firstName, String email,
                           String password) implements Serializable {
}