package ro.unibuc.fmi.karate_auth_service.dtos.coach;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.license.LicenseInfo;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for coach response data, used to return a coach's information.
 * This class includes personal details such as user details and a list of athletes.
 * <p>Usage: This DTO is used for returning coach data from the backend to the client.</p>
 */
@Schema(description = "Coach response containing personal details such as user details and a list of athletes.")
public record CoachResponse(UserResponse user,
                            LicenseInfo licenseInfo,
                            ClubResponse club,
                            @Size(max = 5) Set<AthleteResponse> athletes
) implements Serializable {
}