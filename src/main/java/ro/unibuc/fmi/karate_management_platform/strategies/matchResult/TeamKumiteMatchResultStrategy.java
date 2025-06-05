package ro.unibuc.fmi.karate_management_platform.strategies.matchResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.TeamKumiteMatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.TeamKumiteMatch;
import ro.unibuc.fmi.karate_management_platform.services.CategoryService;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

@Component
@RequiredArgsConstructor
public class TeamKumiteMatchResultStrategy implements MatchResultStrategy {
    private final TeamService teamService;
    private final CategoryService categoryService;
    private final CompetitionService competitionService;

    @Override
    public Match processMatchResult(MatchResultRequest request) {
        TeamKumiteMatchResultRequest kumiteRequest = (TeamKumiteMatchResultRequest) request;

        TeamKumiteMatch match = TeamKumiteMatch.builder()
                .category(categoryService.getCategoryById(request.getCategoryId()))
                .competition(competitionService.findCompetitionById(request.getCompetitionId()))
                .matchType(MatchType.KUMITE_TEAM)
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .build();

        // Process the teams and their points
        kumiteRequest.getTeamPoints().forEach((teamId, points) -> {
            match.getTeamPoints().put(
                    teamService.getTeamById(teamId),
                    points
            );
        });

        // Process the scores and update the match
        kumiteRequest.getTeamScores().forEach((teamId, score) -> {
            match.getTeamScores().put(
                    teamService.getTeamById(teamId),
                    score
            );
        });

        return match;
    }

    @Override
    public boolean supports(MatchResultRequest request) {
        return request instanceof TeamKumiteMatchResultRequest;
    }
} 