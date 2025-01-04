package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.RequestService;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestResource {
    private final RequestService requestService;


    @Operation(summary = "Create a coach creation request", description = "Creates a coach creation request for the logged in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coach creation request created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachCreationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PostMapping("/coach")
    public ResponseEntity<CoachCreationResponse> coachCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CoachRequest coachRequest
    ) {
        return ResponseEntity.ok(requestService.createCoachCreationRequest(user, coachRequest));
    }
}
