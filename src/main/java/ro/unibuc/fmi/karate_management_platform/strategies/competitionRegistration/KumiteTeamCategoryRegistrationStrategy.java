package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamType;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KumiteTeamCategoryRegistrationStrategy implements CompetitionRegistrationStrategy {
    private final TeamService teamService;

    @Override
    public void register(Long id, Competition competition) {
        Team team = teamService.getTeamById(id);
        AgeGroup ageGroup = teamService.getAgeGroup(team, competition.getDate());
        Gender gender = teamService.getGender(team);

        KumiteTeamCategory rotationCategory = findCategory(competition, ageGroup, gender, KumiteTeamType.ROTATION);
        KumiteTeamCategory simpleCategory = findCategory(competition, ageGroup, gender, KumiteTeamType.SIMPLE);

        if (rotationCategory.getTeams().contains(team) || simpleCategory.getTeams().contains(team)) {
            log.info("Team {} is already registered in category {}", team.getId(), rotationCategory.getId());
            throw new IllegalArgumentException("Team is already registered in this category");
        }
        simpleCategory.getTeams().add(team);
        rotationCategory.getTeams().add(team);
        log.info("Team {} registered in rotation category {}", team.getId(), rotationCategory.getId());
        log.info("Team {} registered in simple category {}", team.getId(), simpleCategory.getId());
    }

    private KumiteTeamCategory findCategory(Competition competition, AgeGroup ageGroup, Gender gender, KumiteTeamType type) {
        return competition.getCategories().stream()
                .filter(KumiteTeamCategory.class::isInstance)
                .map(KumiteTeamCategory.class::cast)
                .filter(matchesCriteria(ageGroup, gender, type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No " + type + " category found for team with AgeGroup " + ageGroup + " and Gender " + gender));
    }

    private Predicate<KumiteTeamCategory> matchesCriteria(AgeGroup ageGroup, Gender gender, KumiteTeamType type) {
        return category -> category.getAgeGroup().equals(ageGroup)
                && category.getGender().equals(gender)
                && category.getKumiteTeamType().equals(type);
    }
}
