package ro.unibuc.fmi.karate_management_platform.dtos.competition.category.withAthletes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Base response containing category details and its participants")
public abstract class CategoryWithAthletesResponse {
    @Schema(description = "Category details", implementation = CategoryResponse.class)
    private CategoryResponse category;
} 