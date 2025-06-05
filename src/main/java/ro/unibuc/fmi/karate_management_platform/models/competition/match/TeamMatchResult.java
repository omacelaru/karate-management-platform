package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "team_match_results")
public class TeamMatchResult extends Match {
    @ElementCollection
    @CollectionTable(
        name = "team_match_points",
        joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "team_id")
    @Column(name = "points")
    private Map<Team, Long> teamPoints;
}