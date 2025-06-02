package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

import java.util.Set;

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

        Set<Team> teams = kataTeamCategory.getParticipations().stream()
                .filter(participation -> participation.getCompetition().equals(competition))
                .map(TeamCategoryParticipation::getTeam)
                .collect(java.util.stream.Collectors.toSet());

        if (teams.contains(team)) {
            log.error("Team {} is already registered in kata team category {} competition {}", team.getId(), kataTeamCategory.getId(), competition.getName());
            throw new IllegalArgumentException("Team is already registered in this category");
        } else {
            kataTeamCategory.getParticipations().add(
                    TeamCategoryParticipation.builder()
                            .category(kataTeamCategory)
                            .competition(competition)
                            .team(team)
                            .build()
            );
            log.info("Team {} registered in kata team category {} competition {}", team.getId(), kataTeamCategory.getId(), competition.getName());
        }

    }
}
