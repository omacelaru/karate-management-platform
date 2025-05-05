package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

@Slf4j
@Component
@RequiredArgsConstructor
public class KataTeamCategoryRegistrationStrategy implements CompetitionRegistrationStrategy {

    private final TeamService teamService;

    @Override
    public void register(Long id, Competition competition) {
        Team team = teamService.getTeamById(id);

        AgeGroup ageGroup = teamService.getAgeGroup(team, competition.getDate());
        Gender gender = teamService.getGender(team);

        KataTeamCategory kataTeamCategory = competition.getCategories().stream()
                .filter(category -> category instanceof KataTeamCategory)
                .map(category -> (KataTeamCategory) category)
                .filter(category -> category.getAgeGroup().equals(ageGroup))
                .filter(category -> category.getGender().equals(gender))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No category found for team"));

        if (kataTeamCategory.getTeams().contains(team)) {
            log.info("Team {} is already registered in category {}", team.getId(), kataTeamCategory.getId());
            throw new IllegalArgumentException("Team is already registered in this category");
        } else {
            kataTeamCategory.getTeams().add(team);
            log.info("Team {} registered in category {}", team.getId(), kataTeamCategory.getId());
        }

    }
}
