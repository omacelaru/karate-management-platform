package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.CategoryRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository<? extends Category> categoryRepository;

    public Set<Category> getCategoriesByIds(Set<Long> longs) {
        log.info("Getting categories by ids {}", longs);

        return longs.stream()
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public AgeGroup getAgeGroupByDateOfBirth(LocalDate birthDate) {
        log.info("Getting age group by birth date {}", birthDate);

        // best way to calculate age with month and day of birth
        int age = LocalDate.now().getYear() - birthDate.getYear();
        if (LocalDate.now().getMonthValue() < birthDate.getMonthValue() ||
                (LocalDate.now().getMonthValue() == birthDate.getMonthValue() &&
                        LocalDate.now().getDayOfMonth() < birthDate.getDayOfMonth())) {
            age--;
        }

        if (age < 7) {
            return CHILDREN_U7;
        } else if (age < 10) {
            return CHILDREN_7_9;
        } else if (age < 13) {
            return CHILDREN_10_12;
        } else if (age < 15) {
            return CADETS_13_14;
        } else if (age < 18) {
            return JUNIORS_15_17;
        } else if (age < 35) {
            return SENIORS_18_34;
        } else if (age < 45) {
            return VETERANS_35_44;
        } else if (age < 65) {
            return VETERANS_45_64;
        } else {
            return VETERANS_65_PLUS;
        }
    }

//    public Set<CategoryType> getCategoryTypeByMatchTypes(Set<MatchType> matchTypes) {
//        log.info("Getting category type by match types {}", matchTypes);
//
//        return matchTypes.stream()
//                .map(MatchType::getCategoryType)
//                .collect(Collectors.toSet());
//    }

    public Set<KataIndividualCategory> getKataCategoriesFromCompetition(Competition competition) {
        return competition.getCategories().stream()
                .filter(category -> category instanceof KataIndividualCategory)
                .map(category -> (KataIndividualCategory) category)
                .collect(Collectors.toSet());
    }

    public Set<KumiteIndividualCategory> getKumiteCategoriesFromCompetition(Competition competition) {
        return competition.getCategories().stream()
                .filter(category -> category instanceof KumiteIndividualCategory)
                .map(category -> (KumiteIndividualCategory) category)
                .collect(Collectors.toSet());
    }
}