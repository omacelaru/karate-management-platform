package ro.unibuc.fmi.karate_management_platform.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;
import ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompetitionRegistrationStrategyFactory {
    public CompetitionRegistrationStrategy getStrategy(IndividualCategoryType categoryType) {
        return switch (categoryType) {
            case KATA_INDIVIDUAL -> new KataIndividualCategoryRegistrationStrategy();
            case KUMITE_INDIVIDUAL -> new KumiteIndividualCategoryRegistrationStrategy();
            default -> throw new IllegalArgumentException("Unknown individual category type");
        };
    }

    public CompetitionRegistrationStrategy getStrategy(TeamCategoryType categoryType) {
        return switch (categoryType) {
            case KATA_TEAM -> new KataTeamCategoryRegistrationStrategy();
            case KUMITE_TEAM -> new KumiteTeamCategoryRegistrationStrategy();
            default -> throw new IllegalArgumentException("Unknown team category type");
        };
    }

}
