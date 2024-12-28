package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.CoachService;

@RestController
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
public class CoachResource {
    private final CoachService coachService;

    @Operation(summary = "Get all coaches paged", description = "Returns a list of all coaches paged")
    @ApiResponse(responseCode = "200", description = "Coaches returned successfully")
    @GetMapping
    public ResponseEntity<Page<CoachResponse>> getAllCoaches(Pageable pageable) {
        return ResponseEntity.ok(coachService.getAllCoaches(pageable));
    }

    @Operation(summary = "Get me, coach that is logged in", description = "Returns information about the logged in coach",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coach information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @GetMapping("/me")
    public ResponseEntity<CoachResponse> getMe() throws IncompleteProfileException {
        return ResponseEntity.ok(coachService.getMe());
    }

    @Operation(summary = "Create a new coach", description = "Creates a new coach",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coach created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PostMapping
    public ResponseEntity<CoachResponse> createCoach(@RequestBody CoachRequest coachRequest) {
        return ResponseEntity.ok(coachService.createCoach(coachRequest));
    }
}