package ro.unibuc.fmi.karate_auth_service.resources;

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
import ro.unibuc.fmi.karate_auth_service.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.OrganizerService;

@RestController
@RequestMapping("/api/v1/organizers")
@RequiredArgsConstructor
public class OrganizerResource {
    private final OrganizerService organizerService;

    @Operation(summary = "Get all organizers paged", description = "Returns a list of all organizers paged")
    @ApiResponse(responseCode = "200", description = "Organizers returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<OrganizerResponse>> getAllOrganizers(Pageable pageable) {
        return ResponseEntity.ok(organizerService.getAllOrganizers(pageable));
    }

    @Operation(summary = "Get me, organizer that is logged in", description = "Returns information about the logged in organizer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Organizer information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrganizerResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.ORGANIZER})
    @GetMapping("/me")
    public ResponseEntity<OrganizerResponse> getMe(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(organizerService.getMe(user));
    }

}
