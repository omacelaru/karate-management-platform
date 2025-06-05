package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;

import java.time.LocalDateTime;

/**
 * DTO for {@link Match}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class MatchResponse {
    private Long categoryId;
    private Long competitionId;
    private MatchType matchType;
    private AgeGroup ageGroup;
    private Gender gender;
}