package ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "matchType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = IndividualKataMatchResultRequest.class, name = "KATA_INDIVIDUAL"),
    @JsonSubTypes.Type(value = IndividualKumiteMatchResultRequest.class, name = "KUMITE_INDIVIDUAL"),
    @JsonSubTypes.Type(value = TeamKataMatchResultRequest.class, name = "KATA_TEAM"),
    @JsonSubTypes.Type(value = TeamKumiteMatchResultRequest.class, name = "KUMITE_TEAM")
})
public abstract class MatchResultRequest {
    private Long categoryId;
    private Long competitionId;
    private MatchType matchType;
    private AgeGroup ageGroup;
    private Gender gender;
} 