package ro.unibuc.fmi.karate_management_platform.dtos.competition.category.participation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract sealed class CategoryParticipationResponse permits IndividualCategoryParticipationResponse, TeamCategoryParticipationResponse {
    private Long id;
    private Long competitionId;
    private Integer durationMinutes;
} 