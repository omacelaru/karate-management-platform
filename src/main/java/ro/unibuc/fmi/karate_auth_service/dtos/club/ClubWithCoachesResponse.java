package ro.unibuc.fmi.karate_auth_service.dtos.club;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for club response data, used to return a club's information.
 * This class includes the club's name, acronym, and a list of coaches.
 *
 * <p>Usage: This DTO is used for returning club data from the backend to the client.</p>
 */
@Schema(description = "Club response containing the club's name, acronym, and a list of coaches.")
public record ClubWithCoachesResponse(Long id,
                                      Set<CoachResponse> coaches,
                                      String name,
                                      String acronym) implements Serializable {
}