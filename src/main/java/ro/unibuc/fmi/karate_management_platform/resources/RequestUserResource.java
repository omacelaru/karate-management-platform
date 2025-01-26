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
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.RequestService;

@RestController
@RequestMapping("/api/v1/requests/users")
@RequiredArgsConstructor
public class RequestUserResource {
    private final RequestService requestService;

    //--------------------------------------------------------------------------------
    //---------------------------------- CREATE REQUEST ------------------------------
    //--------------------------------------------------------------------------------
    @Operation(
            summary = "Create a coach creation request",
            description = "Creates a request for coach creation for the logged-in user. This endpoint processes a request to create a coach, accepting detailed input information for the coach's creation.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the coach creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachCreationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create a coach request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PostMapping("/coach")
    public ResponseEntity<RequestInfoResponseInterface> coachCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CoachRequest coachRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.COACH_CREATION, coachRequest));
    }

    @Operation(
            summary = "Create a referee creation request",
            description = "Creates a request for referee creation for the logged-in user. This endpoint processes a request to create a referee, accepting detailed input information for the referee's creation.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the referee creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create a referee request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PostMapping("/referee")
    public ResponseEntity<RequestInfoResponseInterface> refereeCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid RefereeRequest refereeRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.REFEREE_CREATION, refereeRequest));
    }


    @Operation(
            summary = "Create an athlete creation request",
            description = "Creates a request for athlete creation for the logged-in user. This endpoint processes a request to create an athlete, accepting detailed input information for the athlete's creation.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the athlete creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create an athlete request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PostMapping("/athlete")
    public ResponseEntity<RequestInfoResponseInterface> athleteCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AthleteRequest athleteRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.ATHLETE_CREATION, athleteRequest));
    }

    @Operation(
            summary = "Create an organizer creation request",
            description = "Creates a request for organizer creation for the logged-in user. This endpoint processes a request to create an organizer, accepting detailed input information for the organizer's creation.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created the organizer creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authorized to create an organizer request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PostMapping("/organizer")
    public ResponseEntity<RequestInfoResponseInterface> organizerCreation(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid OrganizerRequest organizerRequest
    ) {
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.ORGANIZER_CREATION, organizerRequest));
    }

    //--------------------------------------------------------------------------------
    //---------------------------------- EDIT REQUEST --------------------------------
    //--------------------------------------------------------------------------------

    @Operation(
            summary = "Edit an existing coach creation request",
            description = "Allows the authenticated user to edit an existing coach creation request by providing the request ID and updated details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited the coach creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: User does not have permission to edit the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/coach")
    public ResponseEntity<RequestInfoResponseInterface> editCoachCreationRequest(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CoachRequest coachRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.COACH_CREATION, coachRequest));
    }

    @Operation(
            summary = "Edit an existing referee creation request",
            description = "Allows the authenticated user to edit an existing referee creation request by providing the request ID and updated details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited the referee creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: User does not have permission to edit the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/referee")
    public ResponseEntity<RequestInfoResponseInterface> editRefereeCreationRequest(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid RefereeRequest refereeRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.REFEREE_CREATION, refereeRequest));
    }

    @Operation(
            summary = "Edit an existing athlete creation request",
            description = "Allows the authenticated user to edit an existing athlete creation request by providing the request ID and updated details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited the athlete creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: User does not have permission to edit the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/athlete")
    public ResponseEntity<RequestInfoResponseInterface> editAthleteCreationRequest(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid AthleteRequest athleteRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.ATHLETE_CREATION, athleteRequest));
    }

    @Operation(
            summary = "Edit an existing organizer creation request",
            description = "Allows the authenticated user to edit an existing organizer creation request by providing the request ID and updated details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully edited the organizer creation request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request: The provided data is invalid or incomplete",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: User does not have permission to edit the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/organizer")
    public ResponseEntity<RequestInfoResponseInterface> editOrganizerCreationRequest(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid OrganizerRequest organizerRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.ORGANIZER_CREATION, organizerRequest));
    }
}
