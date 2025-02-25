package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AthleteCompetitionRegistration", description = "Details about the athlete's registration to a competition, including the athlete's ID and match type")
public class AthleteCompetitionRegistration {

    @NotNull
    @Schema(description = "Unique identifier of the athlete", example = "12345")
    private Long athleteId;

    @Builder.Default
    @NotEmpty
    @Schema(description = "Set of individual categories the athlete is registered for", example = "[\"KATA_INDIVIDUAL\", \"KUMITE_INDIVIDUAL\"]")
    private Set<IndividualCategoryType> individualCategories = Collections.emptySet();

    @Builder.Default
    @NotEmpty
    @Schema(description = "Map of team categories and associated team member IDs", example = "{\"KATA_TEAM\": [1001, 1002], \"KUMITE_TEAM\": [2001, 2002]}")
    private Map<TeamCategoryType, Set<Long>> teamCategories = Collections.emptyMap();
}
