package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import io.swagger.v3.oas.annotations.media.Schema;
import ro.unibuc.fmi.karate_management_platform.annotations.MaxCategoriesPerKey;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;

import java.util.Map;
import java.util.Set;

public record CompetitionRegistrationRequest(
        //TODO validate for more than 2 categories
        @MaxCategoriesPerKey
        @Schema(description = "Set of individual categories the athlete is registered for", example = "{\"1\":[\"KATA_INDIVIDUAL\",\"KUMITE_INDIVIDUAL\"],\"2\":[\"KATA_INDIVIDUAL\",\"KUMITE_INDIVIDUAL\"],\"3\":[\"KATA_INDIVIDUAL\"]}")
        Map<Long, Set<IndividualCategoryType>> individualCategories,

        @MaxCategoriesPerKey
        @Schema(description = "Map of team categories and associated team member IDs", example = "{\"1\":[\"KATA_TEAM\",\"KUMITE_TEAM\"],\"2\":[\"KATA_TEAM\",\"KUMITE_TEAM\"],\"3\":[\"KATA_TEAM\"]}")
        Map<Long, Set<TeamCategoryType>> teamCategories) {
}
