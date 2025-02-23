package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Details about the athlete's registration to a competition like the athlete's id and the match type")
public class AthleteCompetitionRegistration {
    //todo Validate this DTO
    private Long athleteId;

    private Set<MatchType> matchTypes;
}
