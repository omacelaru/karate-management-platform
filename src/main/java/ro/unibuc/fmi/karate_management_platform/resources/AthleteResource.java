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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.AthleteService;

@RestController
@RequestMapping("/api/v1/athletes")
@RequiredArgsConstructor
public class AthleteResource {
    private final AthleteService athleteService;

    @Operation(summary = "Get all athletes paged", description = "Returns a list of all athletes paged")
    @ApiResponse(responseCode = "200", description = "Athletes returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<AthleteResponse>> getAllAthletes(Pageable pageable) {
        return ResponseEntity.ok(athleteService.getAllAthletes(pageable));
    }

    @Operation(summary = "Get logged in athlete information",
            description = "Returns information about the logged in athlete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Athlete information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AthleteResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.ATHLETE})
    @GetMapping("/me")
    public ResponseEntity<AthleteResponse> getMe(
            @AuthenticationPrincipal User user
    ) throws IncompleteProfileException {
        return ResponseEntity.ok(athleteService.getMe(user));
    }

    @Operation(summary = "Get athlete's competitions",
            description = "Returns a list of competitions that the athlete has participated in or will participate in",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Competitions returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.ATHLETE})
    @GetMapping("/me/competitions")
    public ResponseEntity<Page<CompetitionResponse>> getMyCompetitions(
            @AuthenticationPrincipal User user,
            Pageable pageable
    ) throws IncompleteProfileException {
        return ResponseEntity.ok(athleteService.getMyCompetitions(user, pageable));
    }
}
