package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "team_kumite_matches")
public class TeamKumiteMatch extends Match {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_left_id", nullable = false)
    private Team teamLeft;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_right_id", nullable = false)
    private Team teamRight;
}
