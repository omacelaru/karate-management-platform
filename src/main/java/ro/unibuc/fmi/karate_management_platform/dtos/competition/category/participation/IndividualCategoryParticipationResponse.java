package ro.unibuc.fmi.karate_management_platform.dtos.competition.category.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public final class IndividualCategoryParticipationResponse extends CategoryParticipationResponse {
    private AthleteResponse athlete;
} 