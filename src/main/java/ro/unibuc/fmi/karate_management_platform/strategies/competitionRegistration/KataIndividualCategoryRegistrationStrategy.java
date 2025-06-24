package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KataIndividualCategoryRegistrationStrategy implements CompetitionRegistrationStrategy {
    private final AthleteService athleteService;

    @Override
    public void register(Long id, Competition competition) {
        Athlete athlete = athleteService.getAthleteById(id);

        AgeGroup ageGroup = athleteService.getAgeGroup(athlete, competition.getDate());
        KataBeltRange kataBeltRange = KataBeltRange.getKataBeltRange(athlete.getBelt());
        Gender gender = athlete.getUser().getGender();


        KataIndividualCategory kataIndividualCategory = competition.getCategories().stream()
                .filter(category -> category instanceof KataIndividualCategory)
                .map(category -> (KataIndividualCategory) category)
                .filter(category -> category.getAgeGroup().equals(ageGroup))
                .filter(category -> category.getKataBeltRange().equals(kataBeltRange))
                .filter(category -> category.getGender().equals(gender))
                .findFirst()
                .orElse(competition.getCategories().stream()
                        .filter(category -> category instanceof KataIndividualCategory)
                        .map(category -> (KataIndividualCategory) category)
                        .filter(category -> category.getAgeGroup().equals(ageGroup))
                        .filter(category -> category.getKataBeltRange().equals(KataBeltRange.OPEN))
                        .filter(category -> category.getGender().equals(gender))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No category found for athlete")));

        Set<Athlete> athletes = kataIndividualCategory.getParticipations()
                .stream()
                .filter(participation -> participation.getCompetition().equals(competition))
                .map(participation -> ((IndividualCategoryParticipation) participation).getAthlete())
                .collect(Collectors.toSet());

        if (athletes.contains(athlete)) {
            log.error("Athlete {} is already registered in kata individual category {} for competition {}", athlete.getId(), kataIndividualCategory.getId(),competition.getName());
            throw new IllegalArgumentException("Athlete is already registered in this category");
        } else {
            kataIndividualCategory.getParticipations().add(
                    IndividualCategoryParticipation.builder()
                            .category(kataIndividualCategory)
                            .competition(competition)
                            .athlete(athlete)
                            .durationMinutes(4)
                            .build()
            );
            log.info("Athlete {} registered in kata individual category {}, competition {}", athlete.getId(), kataIndividualCategory.getId(),competition.getName());

        }
    }
}

