package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_auth_service.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.request.CoachCreationResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestStatus;
import ro.unibuc.fmi.karate_auth_service.models.request.RequestType;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.RequestService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestResource {
    private final RequestService requestService;


    @Operation(
            summary = "Fetch user-specific paginated requests",
            description = "Returns a paginated list of requests made by the currently authenticated user. This endpoint allows the user to retrieve requests in a paginated format for better performance when there are many requests.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the user's requests in a paginated format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @GetMapping("/made-by-me")
    public ResponseEntity<Page<? extends RequestInfoResponseInterface>> getRequestsMadeByMe(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            @RequestParam(defaultValue = "COACH_CREATION") RequestType requestType
    ) {
        return ResponseEntity.ok(requestService.getRequestsMadeByMe(user, pageable, requestType));
    }

    @Operation(
            summary = "Fetch user-specific paginated requests",
            description = "Returns a paginated list of requests assigned to the currently authenticated user. This endpoint allows the user to retrieve requests in a paginated format for better performance when there are many requests.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the user's requests in a paginated format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @GetMapping("/assigned-to-me")
    public ResponseEntity<Page<? extends RequestInfoResponseInterface>> getRequestsAssignedToMe(
            @AuthenticationPrincipal User user,
            Pageable pageable,
            @RequestParam(defaultValue = "COACH_CREATION") RequestType requestType
    ) {
        return ResponseEntity.ok(requestService.getRequestsAssignedToMe(user, pageable, requestType));
    }

    @Operation(
            summary = "Update the status of a request",
            description = "Allows the authenticated user to update the status of a specific request (e.g., ACCEPTED or REJECTED) by providing the request ID and the new status.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated the status of the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RequestInfoResponseInterface.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request or status",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized: User is not authenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden: User does not have permission to update the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
            })
    @SecuredEndpoint
    @PatchMapping("/{requestId}/status")
    public ResponseEntity<RequestInfoResponseInterface> updateRequestStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId,
            @RequestParam(defaultValue = "COACH_CREATION") RequestType requestType,
            @RequestParam(defaultValue = "ACCEPTED") RequestStatus status
    ) {
        return ResponseEntity.ok(requestService.updateRequestStatus(user, requestId, requestType, status));
    }


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
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.COACH_CREATION, coachRequest, Set.of(Role.ADMIN)));
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
        return ResponseEntity.ok(requestService.createRequest(user, RequestType.REFEREE_CREATION, refereeRequest, Set.of(Role.ADMIN)));
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
    @PatchMapping("/coach/{requestId}")
    public ResponseEntity<RequestInfoResponseInterface> editCoachCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId,
            @RequestBody @Valid CoachRequest coachRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.COACH_CREATION, requestId, coachRequest));
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
    @PatchMapping("/referee/{requestId}")
    public ResponseEntity<RequestInfoResponseInterface> editRefereeCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId,
            @RequestBody @Valid RefereeRequest refereeRequest
    ) {
        return ResponseEntity.ok(requestService.editRequest(user, RequestType.REFEREE_CREATION, requestId, refereeRequest));
    }

    //--------------------------------------------------------------------------------
    //---------------------------------- REVOKE REQUEST ------------------------------
    //--------------------------------------------------------------------------------

    @Operation(
            summary = "Revoke an existing coach creation request",
            description = "Allows the authenticated user to revoke an existing coach creation request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully revoke the coach creation request",
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
                            description = "Forbidden: User does not have permission to delete the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @DeleteMapping("/coach/{requestId}")
    public ResponseEntity<RequestInfoResponseInterface> revokeCoachCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.revokeRequest(user, RequestType.COACH_CREATION, requestId));
    }

    @Operation(
            summary = "Revoke an existing referee creation request",
            description = "Allows the authenticated user to revoke an existing referee creation request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully revoked the referee creation request",
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
                            description = "Forbidden: User does not have permission to delete the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @DeleteMapping("/referee/{requestId}")
    public ResponseEntity<RequestInfoResponseInterface> revokeRefereeCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.revokeRequest(user, RequestType.REFEREE_CREATION, requestId));
    }

    //---------------------------------------------------------------------------------
    //---------------------------------- ACTIVATE REQUEST -----------------------------
    //---------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------
    //---------------------------------- ACTIVATE REQUEST ------------------------------
    //--------------------------------------------------------------------------------

    @Operation(
            summary = "Activate an existing coach creation request",
            description = "Allows the authenticated user to activate an existing coach creation request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully activated the coach creation request",
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
                            description = "Forbidden: User does not have permission to activate the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/coach/{requestId}/activate")
    public ResponseEntity<RequestInfoResponseInterface> activateCoachCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.activateRequest(user, RequestType.COACH_CREATION, requestId));
    }

    @Operation(
            summary = "Activate an existing referee creation request",
            description = "Allows the authenticated user to activate an existing referee creation request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully activated the referee creation request",
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
                            description = "Forbidden: User does not have permission to activate the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @PatchMapping("/referee/{requestId}/activate")
    public ResponseEntity<RequestInfoResponseInterface> activateRefereeCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.activateRequest(user, RequestType.REFEREE_CREATION, requestId));
    }

}
