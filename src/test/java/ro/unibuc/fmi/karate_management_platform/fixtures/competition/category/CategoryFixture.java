package ro.unibuc.fmi.karate_management_platform.fixtures.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategoryType;

public class CategoryFixture {

    public static Category createDefaultCategory() {
        return KataCategory.builder()
                .id(1L)
                .kataCategoryType(KataCategoryType.OPEN)
                .build();
    }

    public static Category createCustomCategory(Long id, KataCategoryType kataCategoryType) {
        return KataCategory.builder()
                .id(id)
                .kataCategoryType(kataCategoryType)
                .build();
    }
}