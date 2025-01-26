package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "team_kata_matches")
public class TeamKataMatch extends Match {
    @ManyToMany
    @JoinTable(
            name = "team_kata_matches_teams",
            joinColumns = @JoinColumn(name = "team_kata_match_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> teams;
}
