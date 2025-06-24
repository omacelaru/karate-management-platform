package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "individual_match_results")
public class IndividualMatchResult extends Match {
    @ElementCollection
    @CollectionTable(
        name = "individual_match_points",
        joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "athlete_id")
    @Column(name = "points")
    @Builder.Default
    private Map<Athlete, Long> athletePoints = new HashMap<>();
}