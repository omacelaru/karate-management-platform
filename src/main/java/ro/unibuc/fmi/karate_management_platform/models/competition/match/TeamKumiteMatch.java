package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "team_kumite_matches")
public class TeamKumiteMatch extends TeamMatchResult {
    @ElementCollection
    @CollectionTable(
            name = "team_kumite_scores",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "team_id")
    @Column(name = "score")
    @Size(min = 2, max = 2, message = "Exactly 2 teams are required in a kumite match")
    @Builder.Default
    private Map<Team, Long> teamScores = new HashMap<>();
}
