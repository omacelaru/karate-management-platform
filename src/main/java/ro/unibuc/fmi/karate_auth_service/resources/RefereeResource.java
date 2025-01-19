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
import ro.unibuc.fmi.karate_auth_service.dtos.referee.RefereeResponse;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.RefereeService;

@RestController
@RequestMapping("/api/v1/referees")
@RequiredArgsConstructor
public class RefereeResource {
    private final RefereeService refereeService;

    @Operation(summary = "Get all referees paged", description = "Returns a list of all referees paged")
    @ApiResponse(responseCode = "200", description = "Referees returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<RefereeResponse>> getAllReferees(Pageable pageable) {
        return ResponseEntity.ok(refereeService.getAllReferees(pageable));
    }

    @Operation(summary = "Get me, referee that is logged in", description = "Returns information about the logged in referee",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Referee information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefereeResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefereeResponse.class)))
            })
    @SecuredEndpoint(roles = {Role.REFEREE})
    @GetMapping("/me")
    public ResponseEntity<RefereeResponse> getMe(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(refereeService.getMe(user));
    }
}
