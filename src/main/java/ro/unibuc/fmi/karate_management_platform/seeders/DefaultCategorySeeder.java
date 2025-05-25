package ro.unibuc.fmi.karate_management_platform.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteDivisionRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamType;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KataIndividualCategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KataTeamCategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KumiteIndividualCategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.KumiteTeamCategoryRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DefaultCategorySeeder implements CommandLineRunner {
    private final KataIndividualCategoryRepository kataIndividualCategoryRepository;
    private final KumiteIndividualCategoryRepository kumiteIndividualCategoryRepository;
    private final KataTeamCategoryRepository kataTeamCategoryRepository;
    private final KumiteTeamCategoryRepository kumiteTeamCategoryRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (kataIndividualCategoryRepository.count() == 0 && kumiteIndividualCategoryRepository.count() == 0 && kataTeamCategoryRepository.count() == 0 && kumiteTeamCategoryRepository.count() == 0) {
            seedKataIndividualCategories();
            seedKumiteIndividualCategories();
            seedKataTeamCategories();
            seedKumiteTeamCategories();
        }
    }

    private void seedKataIndividualCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            Set<KataBeltRange> kataBeltRanges = KataBeltRange.getAvailableKataBeltRanges(ageGroup);
            for (Gender gender : Gender.values()) {
                for (KataBeltRange beltRange : kataBeltRanges) {
                    if (!kataIndividualCategoryRepository.existsByAgeGroupAndGenderAndKataBeltRange(ageGroup, gender, beltRange)) {
                        KataIndividualCategory kataCategory = KataIndividualCategory.builder().ageGroup(ageGroup).gender(gender).kataBeltRange(beltRange).categoryType(IndividualCategoryType.KATA_INDIVIDUAL).isDefault(true).build();
                        kataIndividualCategoryRepository.save(kataCategory);
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
                    KumiteIndividualCategory kumiteIndividualCategory = KumiteIndividualCategory.builder().ageGroup(ageGroup).gender(gender).categoryType(IndividualCategoryType.KUMITE_INDIVIDUAL).isDefault(true).kumiteDivisionRange(kumiteDivisionRange).build();
                    kumiteIndividualCategoryRepository.save(kumiteIndividualCategory);
                }
            }
        }
    }

    private void seedKataTeamCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            for (Gender gender : Gender.values()) {
                for (KataTeamType kataTeamType : KataTeamType.values()) {
                    KataTeamCategory kataTeamCategory = KataTeamCategory.builder().ageGroup(ageGroup).gender(gender).categoryType(TeamCategoryType.KATA_TEAM).isDefault(true).kataTeamType(kataTeamType).build();
                    kataTeamCategoryRepository.save(kataTeamCategory);
                }
            }
        }
    }

    private void seedKumiteTeamCategories() {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            for (Gender gender : Gender.values()) {
                for (KumiteTeamType kumite : KumiteTeamType.values()) {
                    KumiteTeamCategory kumiteTeamCategory = KumiteTeamCategory.builder().ageGroup(ageGroup).gender(gender).categoryType(TeamCategoryType.KUMITE_TEAM).isDefault(true).kumiteTeamType(kumite).build();
                    kumiteTeamCategoryRepository.save(kumiteTeamCategory);
                }
            }
        }
    }
}
