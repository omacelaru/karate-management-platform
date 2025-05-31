package ro.unibuc.fmi.karate_management_platform.dtos.competition.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class TeamKataMatchResponse extends MatchResponse {
    private List<TeamResponse> teams;
}
