package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.registration.RegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.CompetitionService;

@RestController
@RequestMapping("/api/v1/competitions")
@RequiredArgsConstructor
public class CompetitionResource {
    private final CompetitionService competitionService;

    @Operation(summary = "Get all competitions paged", description = "Returns a list of all competitions paged")
    @ApiResponse(responseCode = "200", description = "Competitions returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<CompetitionResponse>> getAllCompetitions(Pageable pageable) {
        return ResponseEntity.ok(competitionService.getAllCompetitions(pageable));
    }

    @Operation(
            summary = "Get competition by ID",
            description = "Returns a competition by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Competition returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CompetitionResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Competition not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @GetMapping("/{competitionId}")
    public ResponseEntity<CompetitionResponse> getCompetitionById(@PathVariable Long competitionId) {
        return ResponseEntity.ok(competitionService.getCompetitionById(competitionId));
    }

    @Operation(
            summary = "Register athlete to competition",
            description = "Allows a coach to register an athlete to a competition",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Athlete registered successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CompetitionResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Competition or athlete not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    )
            }
    )
    @SecuredEndpoint(roles = {Role.COACH})
    @PostMapping("/{competitionId}/register")
    public ResponseEntity<CompetitionResponse> registerAthletesToCompetition(
            @AuthenticationPrincipal User user,
            @PathVariable Long competitionId,
            @RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(competitionService.registerAthletesToCompetition(user, competitionId, registrationRequest));
    }
}
