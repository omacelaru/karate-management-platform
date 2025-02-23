package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteDivisionRange;

public interface KumiteCategoryRepository extends CategoryRepository<KumiteCategory> {
    boolean existsByAgeGroupAndGenderAndKumiteDivisionRange(AgeGroup ageGroup, Gender gender, KumiteDivisionRange kumiteDivisionRange);
}