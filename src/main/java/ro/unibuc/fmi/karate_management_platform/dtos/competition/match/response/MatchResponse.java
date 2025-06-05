package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;

import java.time.LocalDateTime;

/**
 * DTO for {@link Match}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract sealed class MatchResponse permits IndividualKataMatchResponse, IndividualKumiteMatchResponse, TeamKataMatchResponse, TeamKumiteMatchResponse {
    private Long id;
    private LocalDateTime scheduledTime;
    private String result;
}