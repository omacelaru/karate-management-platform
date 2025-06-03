package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract sealed class CategoryResponse permits IndividualCategoryResponse, TeamCategoryResponse {
    private Long id;
    private AgeGroup ageGroup;
    private Gender gender;
    private Boolean isDefault;
}
