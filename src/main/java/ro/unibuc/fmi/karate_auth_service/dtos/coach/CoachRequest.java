package ro.unibuc.fmi.karate_auth_service.dtos.coach;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserDetailsRequest;

import java.io.Serializable;

/**
 * DTO for coach request data, used to create or update a coach's information.
 * This class includes personal details such as user details.
 *
 * <p>Usage: This DTO is used for passing coach data from the client to the backend when creating or updating a coach's profile.</p>
 */
@Schema(description = "Coach request containing personal details such as user details.")
public record CoachRequest() implements Serializable {
}