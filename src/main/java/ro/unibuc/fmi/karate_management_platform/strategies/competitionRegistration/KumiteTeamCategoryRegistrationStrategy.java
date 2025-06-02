package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryParticipation;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamType;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

import java.util.Set;
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

        Set<Team> teamsInRotation = rotationCategory.getParticipations().stream()
                .filter(participation -> participation.getCompetition().equals(competition))
                .map(TeamCategoryParticipation::getTeam)
                .collect(java.util.stream.Collectors.toSet());

        Set<Team> teamsInSimple = simpleCategory.getParticipations().stream()
                .filter(participation -> participation.getCompetition().equals(competition))
                .map(TeamCategoryParticipation::getTeam)
                .collect(java.util.stream.Collectors.toSet());

        if (teamsInRotation.contains(team) || teamsInSimple.contains(team)) {
            log.error("Team {} is already registered in kumite team category {} competition {}", team.getId(), rotationCategory.getId(), competition.getName());
            throw new IllegalArgumentException("Team is already registered in this category");
        }

        rotationCategory.getParticipations().add(
                TeamCategoryParticipation.builder()
                        .category(rotationCategory)
                        .competition(competition)
                        .team(team)
                        .build()
        );

        simpleCategory.getParticipations().add(
                TeamCategoryParticipation.builder()
                        .category(simpleCategory)
                        .competition(competition)
                        .team(team)
                        .build()
        );

        log.info("Team {} registered in rotation kumite team category {} competition {}", team.getId(), rotationCategory.getId(), competition.getName());
        log.info("Team {} registered in simple kumite team category {} competition {}", team.getId(), simpleCategory.getId(), competition.getName());
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
