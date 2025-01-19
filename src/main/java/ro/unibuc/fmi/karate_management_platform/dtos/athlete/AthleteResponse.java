package ro.unibuc.fmi.karate_management_platform.dtos.athlete;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;

import java.io.Serializable;

/**
 * DTO for the athlete response data, used for representing the athlete's information
 * returned in API responses. This includes personal details such as name, nationality,
 * birthdate, gender, height, weight, and roles.
 *
 * <p>Usage: This DTO is used when returning athlete data, either after creation or
 * during updates, and it is typically returned in the response body of an API call.</p>
 */
@Schema(description = "Athlete response containing athletes information including roles, personal details, and measurements.")
public record AthleteResponse(UserResponse user,
                              Integer height, Integer weight
) implements Serializable {
}