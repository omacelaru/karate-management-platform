package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataBeltRange;

public interface KataIndividualCategoryRepository extends CategoryRepository<KataIndividualCategory> {
    boolean existsByAgeGroupAndGenderAndKataBeltRange(AgeGroup ageGroup, Gender gender, KataBeltRange kataBeltRange);
}