package ro.unibuc.fmi.karate_management_platform.fixtures.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.individual.KataIndividualCategory;

public class CategoryFixture {

    public static Category createDefaultCategory() {
        return KataIndividualCategory.builder()
                .id(1L)
                .kataBeltRange(KataBeltRange.OPEN)
                .build();
    }

    public static Category createCustomCategory(Long id, KataBeltRange kataBeltRange) {
        return KataIndividualCategory.builder()
                .id(id)
                .kataBeltRange(kataBeltRange)
                .build();
    }
}