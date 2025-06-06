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
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;
import java.util.*;

@Component
@RequiredArgsConstructor
public class TeamKataMatchResultStrategy implements MatchResultStrategy {
    private final TeamService teamService;
    private final CategoryService categoryService;
    private final CompetitionService competitionService;
    private final AthleteService athleteService;

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
        Map<Long, Long> teamPointsMap = kataRequest.getTeamPoints();
        Map<Long, ro.unibuc.fmi.karate_management_platform.models.competition.Team> teamEntities = new HashMap<>();
        teamPointsMap.forEach((teamId, points) -> {
            var team = teamService.getTeamById(teamId);
            teamEntities.put(teamId, team);
            match.getTeamPoints().put(team, points);
            // Update points for each athlete in the team
            for (var athlete : team.getAthletes()) {
                athlete.setPoints(athlete.getPoints() + points.intValue());
            }
        });
        // Process the scores and update the match
        kataRequest.getTeamScores().forEach((teamId, score) -> {
            match.getTeamScores().put(
                    teamEntities.get(teamId),
                    score
            );
        });
        // Sort teams by points desc and assign medals to each athlete in top 3 teams
        List<Map.Entry<Long, Long>> sorted = new ArrayList<>(teamPointsMap.entrySet());
        sorted.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        for (int i = 0; i < sorted.size(); i++) {
            Long teamId = sorted.get(i).getKey();
            var team = teamEntities.get(teamId);
            for (var athlete : team.getAthletes()) {
                if (i == 0) athlete.setGoldMedals(athlete.getGoldMedals() + 1);
                else if (i == 1) athlete.setSilverMedals(athlete.getSilverMedals() + 1);
                else if (i == 2) athlete.setBronzeMedals(athlete.getBronzeMedals() + 1);
                athleteService.updateAthlete(athlete);
            }
        }
        return match;
    }

    @Override
    public boolean supports(MatchResultRequest request) {
        return request instanceof TeamKataMatchResultRequest;
    }
} 