package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
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
@Table(name = "team_kata_matches")
public class TeamKataMatch extends TeamMatchResult {
    @ElementCollection
    @CollectionTable(
            name = "team_kata_scores",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "team_id")
    @Column(name = "score")
    @Builder.Default
    private Map<Team, Long> teamScores = new HashMap<>();
}
