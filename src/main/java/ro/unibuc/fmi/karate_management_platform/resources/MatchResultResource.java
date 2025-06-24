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
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubMedalsResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response.MatchResponse;
import ro.unibuc.fmi.karate_management_platform.factories.MatchResultStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.MatchService;
import ro.unibuc.fmi.karate_management_platform.strategies.matchResult.MatchResultStrategy;

import java.util.List;
import java.util.Optional;

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

    @Operation(
            summary = "Get club medals",
            description = "Returns the medal counts for a given club ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Club medals found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClubMedalsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Club not found",
                    content = @Content
            )
    })
    @SecuredEndpoint
    @GetMapping("/competition/{competitionId}/medals")
    public ResponseEntity<List<ClubMedalsResponse>> getMedalsByClubForCompetition(@PathVariable Long competitionId) {
        return ResponseEntity.ok(matchService.getMedalsByClubForCompetition(competitionId));
    }
} 