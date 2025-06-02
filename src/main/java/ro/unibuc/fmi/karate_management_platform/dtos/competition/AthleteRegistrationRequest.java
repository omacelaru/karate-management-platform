package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;

import java.io.Serializable;
import java.util.Set;

/**
 * Request model for an athlete to register for a competition.
 * This request will be sent to the athlete's coaches for approval.
 */
@Embeddable
@Schema(description = "Request model for an athlete to register for a competition. This request will be sent to the athlete's coaches for approval.")
public record AthleteRegistrationRequest(
        @Schema(
                description = "The unique identifier of the competition the athlete wants to register for",
                example = "1"
        )
        @NotNull
        Long competitionId,

        @Schema(
                description = "Set of individual categories the athlete wants to participate in. Can be null if the athlete only wants to participate in team categories. Maximum 2 categories allowed.",
                example = "[\"KATA_INDIVIDUAL\", \"KUMITE_INDIVIDUAL\"]"
        )
        @Size(max = 2, message = "Maximum 2 individual categories allowed")
        @Enumerated(EnumType.STRING)
        Set<IndividualCategoryType> individualCategories,

        @Schema(
                description = "Set of team categories the athlete wants to participate in. Can be null if the athlete only wants to participate in individual categories. Maximum 2 categories allowed.",
                example = "[\"KATA_TEAM\", \"KUMITE_TEAM\"]"
        )
        @Size(max = 2, message = "Maximum 2 team categories allowed")
        @Enumerated(EnumType.STRING)
        Set<TeamCategoryType> teamCategories
) implements Serializable {
}
