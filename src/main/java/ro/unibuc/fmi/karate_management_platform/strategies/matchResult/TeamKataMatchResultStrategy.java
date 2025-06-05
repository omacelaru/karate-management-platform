package ro.unibuc.fmi.karate_management_platform.strategies.matchResult;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.TeamKataMatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.MatchType;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.TeamKataMatch;
import ro.unibuc.fmi.karate_management_platform.services.CategoryService;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

@Component
@RequiredArgsConstructor
public class TeamKataMatchResultStrategy implements MatchResultStrategy {
    private final TeamService teamService;
    private final CategoryService categoryService;
    private final CompetitionService competitionService;

    @Override
    public Match processMatchResult(MatchResultRequest request) {
        TeamKataMatchResultRequest kataRequest = (TeamKataMatchResultRequest) request;

        TeamKataMatch match = TeamKataMatch.builder()
                .category(categoryService.getCategoryById(request.getCategoryId()))
                .competition(competitionService.findCompetitionById(request.getCompetitionId()))
                .matchType(MatchType.KATA_TEAM)
                .ageGroup(request.getAgeGroup())
                .gender(request.getGender())
                .build();

        // Process the teams and their points
        kataRequest.getTeamPoints().forEach((teamId, points) -> {
            match.getTeamPoints().put(
                    teamService.getTeamById(teamId),
                    points
            );
        });

        // Process the scores and update the match
        kataRequest.getTeamScores().forEach((teamId, score) -> {
            match.getTeamScores().put(
                    teamService.getTeamById(teamId),
                    score
            );
        });

        return match;
    }

    @Override
    public boolean supports(MatchResultRequest request) {
        return request instanceof TeamKataMatchResultRequest;
    }
} 