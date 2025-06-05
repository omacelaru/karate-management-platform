package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class MatchResultRequest {
    private Long matchId;
    private Long categoryId;
    private Long competitionId;
    private MatchType matchType;
    private AgeGroup ageGroup;
    private Gender gender;
} 