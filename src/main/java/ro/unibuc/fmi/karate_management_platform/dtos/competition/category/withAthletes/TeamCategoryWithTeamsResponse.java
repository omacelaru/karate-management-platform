package ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing team category details and its assigned teams")
public class TeamCategoryWithTeamsResponse extends CategoryWithAthletesResponse {
    @Schema(description = "List of teams assigned to this category")
    private List<TeamResponse> teams;
} 