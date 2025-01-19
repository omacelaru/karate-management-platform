package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
@DiscriminatorValue("TEAM")
public class TeamMatch extends Match {

    @ManyToMany
    @JoinTable(
            name = "team_left_athletes",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private Set<Athlete> teamLeft;

    @ManyToMany
    @JoinTable(
            name = "team_right_athletes",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private Set<Athlete> teamRight;
}