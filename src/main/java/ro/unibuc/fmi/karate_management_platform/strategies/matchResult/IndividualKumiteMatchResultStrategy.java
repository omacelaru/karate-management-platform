package ro.unibuc.fmi.karate_management_platform.strategies.matchResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.IndividualKumiteMatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.IndividualKumiteMatch;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;
import ro.unibuc.fmi.karate_management_platform.services.CategoryService;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;

@Component
@RequiredArgsConstructor
public class IndividualKumiteMatchResultStrategy implements MatchResultStrategy {
    private final AthleteService athleteService;
    private final CategoryService categoryService;
    private final CompetitionService competitionService;

    @Override
    public Match processMatchResult(MatchResultRequest request) {
        IndividualKumiteMatchResultRequest kumiteRequest = (IndividualKumiteMatchResultRequest) request;
        
        IndividualKumiteMatch match = IndividualKumiteMatch.builder()
                .category(categoryService.getCategoryById(request.getCategoryId()))
                .competition(competitionService.findCompetitionById(request.getCompetitionId()))
                .matchType(MatchType.INDIVIDUAL_KUMITE)
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .build();

        // Process the athletes and their points
        kumiteRequest.getAthletePoints().forEach((athleteId, points) -> {
            match.getAthletePoints().put(
                athleteService.getAthleteById(athleteId),
                points
            );
        });

        // Process the scores and update the match
        kumiteRequest.getAthleteScores().forEach((athleteId, score) -> {
            match.getAthletePoints().put(
                athleteService.getAthleteById(athleteId),
                score
            );
        });

        return match;
    }

    @Override
    public boolean supports(MatchResultRequest request) {
        return request instanceof IndividualKumiteMatchResultRequest && 
               request.getMatchType() == MatchType.INDIVIDUAL_KUMITE;
    }
} 