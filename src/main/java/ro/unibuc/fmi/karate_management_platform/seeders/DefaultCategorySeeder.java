package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.KataCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteCategory;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KataCategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KumiteCategoryRepository;

@Component
@RequiredArgsConstructor
public class DefaultCategorySeeder implements CommandLineRunner {
    private final KataCategoryRepository kataCategoryRepository;
    private final KumiteCategoryRepository kumiteCategoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedKataCategories();
        seedKumiteCategories();
    }

    private void seedKataCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            for (Gender gender : Gender.values()) {
                for (KataCategoryType kataType : KataCategoryType.values()) {
                    if (!kataCategoryRepository.existsByAgeGroupAndGenderAndKataCategoryType(ageGroup, gender, kataType)) {
                        KataCategory kataCategory = KataCategory.builder()
                                .ageGroup(ageGroup)
                                .gender(gender)
                                .kataCategoryType(kataType)
                                .isDefault(true)
                                .build();
                        kataCategoryRepository.save(kataCategory);
                    }
                }
            }
        }
    }

    private void seedKumiteCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            for (Gender gender : Gender.values()) {
                short[][] applicableWeightRanges = getWeightRangesForGenderAndAge(gender, ageGroup);

                for (short[] weightRange : applicableWeightRanges) {
                    short weightMin = weightRange[0];
                    short weightMax = weightRange[1];
                    if (!kumiteCategoryRepository.existsByAgeGroupAndGenderAndWeightMinAndWeightMax(
                            ageGroup, gender, weightMin, weightMax)) {
                        KumiteCategory kumiteCategory = KumiteCategory.builder()
                                .ageGroup(ageGroup)
                                .gender(gender)
                                .weightMin(weightMin)
                                .weightMax(weightMax)
                                .isDefault(true)
                                .build();
                        kumiteCategoryRepository.save(kumiteCategory);
                    }
                }
            }
        }
    }

    private short[][] getWeightRangesForGenderAndAge(Gender gender, AgeGroup ageGroup) {
        if (ageGroup == AgeGroup.CHILDREN_U7 || ageGroup == AgeGroup.CHILDREN_7_9 || ageGroup == AgeGroup.CHILDREN_10_12) {
            return new short[][]{
                    {-1, 19}, {20, 25}, {26, 30}, {31, 35}, {36, 40}, {41, 45}, {46, -1}
            };
        }
        if (gender == Gender.FEMALE) {
            return new short[][]{
                    {-1, 40}, {40, 45}, {46, 50}, {51, 55}, {56, 60}, {61, 65}, {66, 70}, {71, -1}
            };
        }
        return new short[][]{
                {-1, 50}, {50, 55}, {56, 60}, {61, 65}, {66, 70}, {71, 75}, {76, 80}, {81, 85}, {85, -1}
        };
    }
}
