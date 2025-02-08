package ro.unibuc.fmi.karate_management_platform.dtos.competition.match;

import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Match}
 */
public record MatchResponse(Long id, LocalDateTime scheduledTime, String result) implements Serializable {
}