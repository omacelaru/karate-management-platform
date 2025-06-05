package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "individual_kata_matches")
public class IndividualKataMatch extends IndividualMatchResult {
    @ElementCollection
    @CollectionTable(
            name = "individual_kata_scores",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "athlete_id")
    @Column(name = "score")
    @Builder.Default
    private Map<Athlete, Double> athleteScores = new HashMap<>();
}
