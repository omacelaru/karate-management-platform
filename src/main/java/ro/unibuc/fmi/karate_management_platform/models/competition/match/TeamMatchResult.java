package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.eclipse.angus.mail.util.BASE64DecoderStream;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.util.HashMap;
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
    @Builder.Default
    private Map<Team, Long> teamPoints = new HashMap<>();
}