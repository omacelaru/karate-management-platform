package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response.MatchResponse;
import ro.unibuc.fmi.karate_management_platform.factories.MatchResultStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.MatchService;
import ro.unibuc.fmi.karate_management_platform.strategies.matchResult.MatchResultStrategy;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;

@RestController
@RequestMapping("/api/v1/match-results")
@RequiredArgsConstructor
public class MatchResultResource {
    private final MatchResultStrategyFactory strategyFactory;
    private final MatchService matchService;

    @Operation(
            summary = "Submit match result",
            description = "Submit a new match result. The endpoint supports different types of matches (Individual/Team Kata/Kumite) " +
                    "and will automatically select the appropriate strategy based on the match type."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Match result successfully submitted",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Match.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - The request body is invalid or the match type is not supported",
                    content = @Content
            )
    })
    @SecuredEndpoint(roles = {Role.ADMIN, Role.REFEREE})
    @PostMapping
    public ResponseEntity<Match> submitMatchResult(@RequestBody MatchResultRequest request) {
        MatchResultStrategy strategy = strategyFactory.getStrategy(request);
        Match updatedMatch = strategy.processMatchResult(request);
        return ResponseEntity.ok(matchService.saveMatch(updatedMatch));
    }

    @Operation(
        summary = "Get match result for a competition and category",
        description = "Returns the match result for a given competitionId and categoryId."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Match result found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MatchResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Match not found",
            content = @Content
        )
    })
    @SecuredEndpoint
    @GetMapping("/competition/{competitionId}/category/{categoryId}")
    public ResponseEntity<Optional<MatchResponse>> getMatchResultByCompetitionAndCategory(
            @PathVariable Long competitionId,
            @PathVariable Long categoryId) {
        return ResponseEntity.ok(matchService.findByCompetitionIdAndCategoryId(competitionId, categoryId));
    }

    @Data
    @AllArgsConstructor
    class ClubMedalsResponse {
        private ClubResponse club;
        private int gold;
        private int silver;
        private int bronze;
    }

    @Operation(
        summary = "Get medals by club for a competition",
        description = "Returns a list of clubs and their number of gold, silver, and bronze medals for a given competition."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Medals by club",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClubMedalsResponse.class)
            )
        )
    })
    @SecuredEndpoint
    @GetMapping("/competition/{competitionId}/medals")
    public ResponseEntity<List<ClubMedalsResponse>> getMedalsByClubForCompetition(@PathVariable Long competitionId) {
        // 1. Get all matches for the competition
        List<Match> matches = matchService.findAllByCompetitionId(competitionId);
        // 2. Map: clubId -> ClubMedalsResponse
        Map<Long, ClubMedalsResponse> clubMedalsMap = new HashMap<>();
        for (Match match : matches) {
            List<Object[]> podium = matchService.getPodiumForMatch(match); // returns list of [club, place]
            for (int i = 0; i < podium.size(); i++) {
                ClubResponse club = (ClubResponse) podium.get(i)[0];
                int place = (int) podium.get(i)[1];
                clubMedalsMap.putIfAbsent(club.getId(), new ClubMedalsResponse(club, 0, 0, 0));
                ClubMedalsResponse resp = clubMedalsMap.get(club.getId());
                if (place == 1) resp.gold++;
                else if (place == 2) resp.silver++;
                else if (place == 3) resp.bronze++;
            }
        }
        return ResponseEntity.ok(new ArrayList<>(clubMedalsMap.values()));
    }
} 