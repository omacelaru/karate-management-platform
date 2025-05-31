package ro.unibuc.fmi.karate_management_platform.dtos.competition.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new team")
public class TeamRequest {
    @NotBlank(message = "Team name is required")
    @Schema(description = "Name of the team", example = "Team Alpha")
    private String teamName;

    @Size(min = 1, max = 4, message = "Team must have between 1 and 4 athletes")
    @Schema(description = "Set of athlete IDs to be included in the team", example = "[1, 2, 3, 4]")
    private Set<Long> athleteIds;
} 