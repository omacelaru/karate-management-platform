package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IndividualKataMatchResultRequest extends IndividualMatchResultRequest {
    private Map<Long, Double> athleteScores; // Map<Athlete ID, Score>
} 