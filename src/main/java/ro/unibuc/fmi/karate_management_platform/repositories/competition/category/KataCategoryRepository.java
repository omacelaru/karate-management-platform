package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataBeltRange;

public interface KataCategoryRepository extends CategoryRepository<KataCategory> {
    boolean existsByAgeGroupAndGenderAndKataBeltRange(AgeGroup ageGroup, Gender gender, KataBeltRange kataBeltRange);
}