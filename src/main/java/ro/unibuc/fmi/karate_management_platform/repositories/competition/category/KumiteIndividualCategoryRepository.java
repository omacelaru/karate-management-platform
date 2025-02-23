package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteDivisionRange;

public interface KumiteIndividualCategoryRepository extends CategoryRepository<KumiteIndividualCategory> {
    boolean existsByAgeGroupAndGenderAndKumiteDivisionRange(AgeGroup ageGroup, Gender gender, KumiteDivisionRange kumiteDivisionRange);
}