package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.AthleteService;

@RestController
@RequestMapping("/api/v1/athletes")
@RequiredArgsConstructor
public class AthleteResource {
    private final AthleteService athleteService;

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


    @Operation(summary = "Create a new athlete",
            description = "Creates a new athlete",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Athlete created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AthleteResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PostMapping("/me")
    public ResponseEntity<AthleteResponse> createAthleteProfile(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AthleteRequest athleteRequest
    ) {
        return ResponseEntity.ok(athleteService.createAthlete(user, athleteRequest));
    }
}
