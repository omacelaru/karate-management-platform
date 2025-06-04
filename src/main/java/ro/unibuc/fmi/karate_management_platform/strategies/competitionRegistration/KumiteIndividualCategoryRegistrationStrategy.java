package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteDivisionRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KumiteIndividualCategoryRegistrationStrategy implements CompetitionRegistrationStrategy {
    private final AthleteService athleteService;

    @Override
    public void register(Long id, Competition competition) {
        Athlete athlete = athleteService.getAthleteById(id);

        AgeGroup ageGroup = athleteService.getAgeGroup(athlete, competition.getDate());
        Gender gender = athlete.getUser().getGender();

        KumiteDivisionRange kumiteDivisionRange = KumiteDivisionRange.getKumiteDivisionRange(athlete, ageGroup);

        KumiteIndividualCategory kumiteIndividualCategory = competition.getCategories().stream()
                .filter(category -> category instanceof KumiteIndividualCategory)
                .map(category -> (KumiteIndividualCategory) category)
                .filter(category -> category.getAgeGroup().equals(ageGroup))
                .filter(category -> category.getKumiteDivisionRange().equals(kumiteDivisionRange))
                .filter(category -> category.getGender().equals(gender))
                .findFirst()
                .orElse(competition.getCategories().stream()
                        .filter(category -> category instanceof KumiteIndividualCategory)
                        .map(category -> (KumiteIndividualCategory) category)
                        .filter(category -> category.getAgeGroup().equals(ageGroup))
                        .filter(category -> category.getKumiteDivisionRange().equals(KumiteDivisionRange.OPEN))
                        .filter(category -> category.getGender().equals(gender))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No category found for athlete")));


        Set<Athlete> athletes = kumiteIndividualCategory.getParticipations()
                .stream()
                .filter(participation -> participation.getCompetition().equals(competition))
                .map(IndividualCategoryParticipation::getAthlete)
                .collect(Collectors.toSet());

        if (athletes.contains(athlete)) {
            log.error("Athlete {} is already registered in kumnite individual category {} for competition {}", athlete.getId(), kumiteIndividualCategory.getId(),competition.getName());
            throw new IllegalArgumentException("Athlete is already registered in this category");
        } else {
            kumiteIndividualCategory.getParticipations().add(
                    IndividualCategoryParticipation.builder()
                            .category(kumiteIndividualCategory)
                            .competition(competition)
                            .athlete(athlete)
                            .build()
            );
            kumiteIndividualCategory.calculateDuration();
            log.info("Athlete {} registered in kumite individual category {}, competition {}", athlete.getId(), kumiteIndividualCategory.getId(),competition.getName());
        }
    }
}
