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
import ro.unibuc.fmi.karate_management_platform.dtos.competition.AthleteRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.request.competition.AthleteCompetitionRegistrationRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.RequestService;

@RestController
@RequestMapping("/api/v1/requests/competitions")
@RequiredArgsConstructor
public class RequestCompetitionResource {
    private final RequestService requestService;

    //--------------------------------------------------------------------------------
    //---------------------------------- CREATE REQUEST ------------------------------
    //--------------------------------------------------------------------------------
    @Operation(
            summary = "Create a competition request",
            description = "Create a request for a competition creation for the logged in user ORGANIZER",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the competition creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachCreationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create a competition request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint(roles = {Role.ORGANIZER})
    @PostMapping
    public ResponseEntity<RequestInfoResponseInterface> competitionCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CompetitionRequest competitionRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.COMPETITION_CREATION, competitionRequest));
    }

    //--------------------------------------------------------------------------------
    //---------------------------------- EDIT REQUEST --------------------------------
    //--------------------------------------------------------------------------------

    @Operation(
            summary = "Edit a competition request",
            description = "Edit a request for a competition creation for the logged in user ORGANIZER",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited the competition creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachCreationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to edit the competition request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint(roles = {Role.ORGANIZER})
    @PatchMapping
    public ResponseEntity<RequestInfoResponseInterface> editCompetitionCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CompetitionRequest competitionRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.COMPETITION_CREATION, competitionRequest));
    }

    @Operation(
            summary = "Register for a competition",
            description = "Create a request to register for a competition as an ATHLETE",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the competition registration request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create a competition registration request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint(roles = {Role.ATHLETE})
    @PostMapping("/register")
    public ResponseEntity<RequestInfoResponseInterface> registerForCompetition(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AthleteRegistrationRequest registrationRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.ATHLETE_COMPETITION_REGISTRATION, registrationRequest));
    }
}
