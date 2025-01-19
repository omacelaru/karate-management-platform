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
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.CoachService;

@RestController
@RequestMapping("/api/v1/coaches")
@RequiredArgsConstructor
public class CoachResource {
    private final CoachService coachService;

    @Operation(summary = "Get all coaches paged", description = "Returns a list of all coaches paged")
    @ApiResponse(responseCode = "200", description = "Coaches returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<CoachResponse>> getAllCoaches(Pageable pageable) {
        return ResponseEntity.ok(coachService.getAllCoaches(pageable));
    }

    @Operation(summary = "Get me, coach that is logged in", description = "Returns information about the logged in coach",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coach information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoachResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.COACH})
    @GetMapping("/me")
    public ResponseEntity<CoachResponse> getMe(
            @AuthenticationPrincipal User user
    ) throws IncompleteProfileException {
        return ResponseEntity.ok(coachService.getMe(user));
    }
}