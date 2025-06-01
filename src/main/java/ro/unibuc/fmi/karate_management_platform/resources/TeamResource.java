package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.team.TeamResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.TeamService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamResource {
    private final TeamService teamService;

    @Operation(
            summary = "Create a new team",
            description = "Allows a coach to create a new team with up to 4 athletes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Team created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: Invalid team data or too many athletes",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create a team",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    )
            }
    )
    @SecuredEndpoint(roles = {Role.COACH})
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid TeamRequest teamRequest
    ) {
        return ResponseEntity.ok(teamService.createTeam(user, teamRequest));
    }

    @Operation(
            summary = "Get all teams",
            description = "Retrieves all teams for the authenticated coach",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Teams retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TeamResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to view teams",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiError.class)
                            )
                    )
            }
    )
    @SecuredEndpoint(roles = {Role.COACH})
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(teamService.getAllTeams(user));
    }
} 