package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;

import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IndividualMatchResultRequest extends MatchResultRequest {
    private Map<Long, Long> athletePoints;
} 