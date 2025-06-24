package ro.unibuc.fmi.karate_management_platform.strategies.matchResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.IndividualKataMatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.IndividualKataMatch;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;
import ro.unibuc.fmi.karate_management_platform.services.CategoryService;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import java.util.*;

@Component
@RequiredArgsConstructor
public class IndividualKataMatchResultStrategy implements MatchResultStrategy {
    private final AthleteService athleteService;
    private final CategoryService categoryService;
    private final CompetitionService competitionService;

    @Override
    public Match processMatchResult(MatchResultRequest request) {
        IndividualKataMatchResultRequest kataRequest = (IndividualKataMatchResultRequest) request;

        IndividualKataMatch match = IndividualKataMatch.builder()
                .category(categoryService.getCategoryById(request.getCategoryId()))
                .competition(competitionService.findCompetitionById(request.getCompetitionId()))
                .matchType(MatchType.KATA_INDIVIDUAL)
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .build();

        // Process the athletes and their points
        Map<Long, Long> athletePointsMap = kataRequest.getAthletePoints();

        Map<Long, ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete> athleteEntities = new HashMap<>();
        athletePointsMap.forEach((athleteId, points) -> {
            var athlete = athleteService.getAthleteById(athleteId);
            athleteEntities.put(athleteId, athlete);
            match.getAthletePoints().put(athlete, points);
            // Update points (add to existing)
            athlete.setPoints(athlete.getPoints() + points.intValue());
        });

        // Process the scores and update the match
        kataRequest.getAthleteScores().forEach((athleteId, score) -> {
            match.getAthleteScores().put(
                    athleteEntities.get(athleteId),
                    score
            );
        });
        // Sort athletes by points desc and assign medals
        List<Map.Entry<Long, Long>> sorted = new ArrayList<>(athletePointsMap.entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        for (int i = 0; i < sorted.size(); i++) {
            Long athleteId = sorted.get(i).getKey();
            var athlete = athleteEntities.get(athleteId);
            if (i == 0) athlete.setGoldMedals(athlete.getGoldMedals() + 1);
            else if (i == 1) athlete.setSilverMedals(athlete.getSilverMedals() + 1);
            else if (i == 2) athlete.setBronzeMedals(athlete.getBronzeMedals() + 1);
            athleteService.updateAthlete(athlete);
        }
        return match;
    }

    @Override
    public boolean supports(MatchResultRequest request) {
        return request instanceof IndividualKataMatchResultRequest;
    }
} 