package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.MatchResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract sealed class CategoryResponse permits KataCategoryResponse, KumiteCategoryResponse {
    private Long id;
    private AgeGroup ageGroup;
    private Gender gender;
    private Boolean isDefault;
}
