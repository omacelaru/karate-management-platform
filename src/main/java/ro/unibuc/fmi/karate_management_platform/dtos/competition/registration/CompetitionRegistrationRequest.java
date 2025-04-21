package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;

import java.util.Map;

public record CompetitionRegistrationRequest(
        //TODO validate for more than 2 categories
        @Size(max = 2, message = "Maximum two individual categories are allowed")
        @Schema(description = "Set of individual categories the athlete is registered for", example = "[\"KATA_INDIVIDUAL\", \"KUMITE_INDIVIDUAL\"]")
        Map<Long,IndividualCategoryType> individualCategories,

        @Size(max = 2, message = "Maximum two team categories are allowed")
        @Schema(description = "Map of team categories and associated team member IDs", example = "{\"KATA_TEAM\": [1001, 1002], \"KUMITE_TEAM\": [2001, 2002]}")
        Map<Long, TeamCategoryType> teamCategories) {
}
