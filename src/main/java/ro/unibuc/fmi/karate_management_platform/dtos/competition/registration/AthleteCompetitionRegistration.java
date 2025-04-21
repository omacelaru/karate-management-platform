package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
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
    @Builder.Default
    //TODO validate for more than 2 categories
    @Size(max = 2, message = "Maximum two individual categories are allowed")
    @Schema(description = "Set of individual categories the athlete is registered for", example = "[\"KATA_INDIVIDUAL\", \"KUMITE_INDIVIDUAL\"]")
    private Map<IndividualCategoryType, Long> individualCategories = Collections.emptyMap();

    @Builder.Default
    @Size(max = 2, message = "Maximum two team categories are allowed")
    @Schema(description = "Map of team categories and associated team member IDs", example = "{\"KATA_TEAM\": [1001, 1002], \"KUMITE_TEAM\": [2001, 2002]}")
    private Map<TeamCategoryType, Set<Long>> teamCategories = Collections.emptyMap();
}
