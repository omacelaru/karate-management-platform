package ro.unibuc.fmi.karate_management_platform.dtos.competition.team;

import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Team}
 */
public record TeamResponse(Long id, String teamName, Set<AthleteResponse> athletes) implements Serializable {
}