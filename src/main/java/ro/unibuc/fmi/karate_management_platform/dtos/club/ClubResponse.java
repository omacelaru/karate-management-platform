package ro.unibuc.fmi.karate_management_platform.dtos.club;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DTO for club response data, used to return a club's information.
 * This class includes the club's name and acronym.
 * <p>Usage: This DTO is used for returning club data from the backend to the client.</p>
 */
@Schema(description = "Club response containing the club's name and acronym.")
public record ClubResponse(Long id,
                           String name,
                           String acronym
) implements Serializable, ClubResponseInterface {
}