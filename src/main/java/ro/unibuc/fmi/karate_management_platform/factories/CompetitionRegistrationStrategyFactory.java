package ro.unibuc.fmi.karate_management_platform.factories;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.IndividualCategoryType;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.TeamCategoryType;
import ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompetitionRegistrationStrategyFactory {
    private final List<CompetitionRegistrationStrategy> strategies;
    private final Map<IndividualCategoryType, CompetitionRegistrationStrategy> individualStrategyMap = new EnumMap<>(IndividualCategoryType.class);
    private final Map<TeamCategoryType, CompetitionRegistrationStrategy> teamStrategyMap = new EnumMap<>(TeamCategoryType.class);

    @PostConstruct
    public void init() {
        for (CompetitionRegistrationStrategy strategy : strategies) {
            if (strategy instanceof KataIndividualCategoryRegistrationStrategy) {
                individualStrategyMap.put(IndividualCategoryType.KATA_INDIVIDUAL, strategy);
            } else if (strategy instanceof KumiteIndividualCategoryRegistrationStrategy) {
                individualStrategyMap.put(IndividualCategoryType.KUMITE_INDIVIDUAL, strategy);
            } else if (strategy instanceof KataTeamCategoryRegistrationStrategy) {
                teamStrategyMap.put(TeamCategoryType.KATA_TEAM, strategy);
            } else if (strategy instanceof KumiteTeamCategoryRegistrationStrategy) {
                teamStrategyMap.put(TeamCategoryType.KUMITE_TEAM, strategy);
            }
        }
    }

    public CompetitionRegistrationStrategy getStrategy(IndividualCategoryType categoryType) {
        CompetitionRegistrationStrategy strategy = individualStrategyMap.get(categoryType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown individual category type");
        }
        return strategy;
    }

    public CompetitionRegistrationStrategy getStrategy(TeamCategoryType categoryType) {
        CompetitionRegistrationStrategy strategy = teamStrategyMap.get(categoryType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown team category type");
        }
        return strategy;
    }

}
