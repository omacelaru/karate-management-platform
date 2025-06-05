package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "individual_kumite_matches")
public class IndividualKumiteMatch extends IndividualMatchResult {
    @ElementCollection
    @CollectionTable(
            name = "individual_kumite_scores",
            joinColumns = @JoinColumn(name = "match_id")
    )
    @MapKeyJoinColumn(name = "athlete_id")
    @Column(name = "score")
    @Size(min = 2, max = 2, message = "Exactly 2 athletes are required in a kumite match")
    private Map<Athlete, Long> athleteScores;
}