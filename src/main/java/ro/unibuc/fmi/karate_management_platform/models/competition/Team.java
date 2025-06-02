package ro.unibuc.fmi.karate_management_platform.models.competition;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.BaseEntity;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategory;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "teams")
public class Team extends BaseEntity {
    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ManyToMany
    @JoinTable(
            name = "team_athletes",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private Set<Athlete> athletes = new LinkedHashSet<>();
}