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
import ro.unibuc.fmi.karate_management_platform.dtos.request.RequestInfoResponseInterface;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestStatus;
import ro.unibuc.fmi.karate_management_platform.models.request.RequestType;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.RequestService;

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
            @RequestParam(defaultValue = "ACCEPTED") RequestStatus status
    ) {
        return ResponseEntity.ok(requestService.updateRequestStatus(user, requestId, status));
    }

    @Operation(
            summary = "Revoke an existing request",
            description = "Allows the authenticated user to revoke an existing request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully revoked the request",
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
                            description = "Forbidden: User does not have permission to revoke the request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Request not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    )
            })
    @SecuredEndpoint
    @DeleteMapping("/{requestId}")
    public ResponseEntity<RequestInfoResponseInterface> revokeRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.revokeRequest(user, requestId));
    }

    @Operation(
            summary = "Activate an existing request",
            description = "Allows the authenticated user to activate an existing request by providing the request ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully activated the request",
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
    @PatchMapping("/{requestId}/activate")
    public ResponseEntity<RequestInfoResponseInterface> activateCoachCreationRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(requestService.activateRequest(user, requestId));
    }
}
