package ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing individual category details and its assigned athletes")
public class IndividualCategoryWithAthletesResponse extends CategoryWithAthletesResponse {
    @Schema(description = "List of athletes assigned to this category, ordered alphabetically")
    private List<AthleteResponse> athletes;
} 