package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "individual_kata_matches")
public class IndividualKataMatch extends Match {
    @ManyToMany
    @JoinTable(
            name = "individual_kata_athletes",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private Set<Athlete> athletes;
}
