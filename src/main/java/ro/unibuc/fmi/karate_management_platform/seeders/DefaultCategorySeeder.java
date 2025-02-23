package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.individual.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.individual.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteDivisionRange;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KataCategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KumiteCategoryRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DefaultCategorySeeder implements CommandLineRunner {
    private final KataCategoryRepository kataCategoryRepository;
    private final KumiteCategoryRepository kumiteCategoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (kataCategoryRepository.count() == 0 && kumiteCategoryRepository.count() == 0) {
            seedKataIndividualCategories();
            seedKumiteIndividualCategories();
        }
    }

    private void seedKataIndividualCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            Set<KataBeltRange> kataBeltRanges = KataBeltRange.getAvailableKataBeltRanges(ageGroup);
            for (Gender gender : Gender.values()) {
                for (KataBeltRange beltRange : kataBeltRanges) {
                    if (!kataCategoryRepository.existsByAgeGroupAndGenderAndKataBeltRange(ageGroup, gender, beltRange)) {
                        KataIndividualCategory kataCategory = KataIndividualCategory.builder().ageGroup(ageGroup).gender(gender).kataBeltRange(beltRange).categoryType(CategoryType.KATA_INDIVIDUAL).isDefault(true).build();
                        kataCategoryRepository.save(kataCategory);
                    }
                }
            }
        }
    }

    private void seedKumiteIndividualCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            for (Gender gender : Gender.values()) {
                Set<KumiteDivisionRange> kumiteDivisionRanges = KumiteDivisionRange.getAvailableDivisions(ageGroup, gender);
                for (KumiteDivisionRange kumiteDivisionRange : kumiteDivisionRanges) {
                    KumiteIndividualCategory kumiteIndividualCategory = KumiteIndividualCategory.builder().ageGroup(ageGroup).gender(gender).categoryType(CategoryType.KUMITE_INDIVIDUAL).isDefault(true).kumiteDivisionRange(kumiteDivisionRange).build();
                    kumiteCategoryRepository.save(kumiteIndividualCategory);
                }
            }
        }
    }

}
