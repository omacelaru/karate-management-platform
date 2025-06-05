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
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.factories.MatchResultStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.MatchService;
import ro.unibuc.fmi.karate_management_platform.strategies.matchResult.MatchResultStrategy;

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
} 