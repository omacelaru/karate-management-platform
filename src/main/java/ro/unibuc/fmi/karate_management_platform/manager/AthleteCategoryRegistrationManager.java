package ro.unibuc.fmi.karate_management_platform.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.CompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.factories.CompetitionRegistrationStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AthleteCategoryRegistrationManager {
    private final CompetitionRegistrationStrategyFactory strategyFactory;
    private final CompetitionRepository competitionRepository;

    public Competition registerAthletesToCompetition(CompetitionRegistrationRequest competitionRegistrationRequest, Coach coach, Competition competition) {
        competitionRegistrationRequest.individualCategories().forEach(
                (athleteId, individualCategoryType) -> {
                    var strategy = strategyFactory.getStrategy(individualCategoryType);
                    strategy.register(athleteId, competition);
                }
        );
        competitionRegistrationRequest.teamCategories().forEach(
                (teamId, teamCategoryType) -> {
                    var strategy = strategyFactory.getStrategy(teamCategoryType);
                    strategy.register(teamId, competition);
                }
        );
        return competitionRepository.save(competition);
    }
}
