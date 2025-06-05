package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class TeamKumiteMatchResponse extends TeamMatchResultResponse {
    private Map<TeamResponse, Long> athleteScores; // Map<Athlete ID, Score>
}
