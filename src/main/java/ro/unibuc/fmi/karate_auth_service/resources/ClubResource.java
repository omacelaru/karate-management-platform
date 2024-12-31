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
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.ClubService;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubResource {
    private final ClubService clubService;

    @Operation(summary = "Get all clubs paginated",
            description = "Get all clubs paginated with the given page and size")
    @ApiResponse(responseCode = "200", description = "Return all clubs paginated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<ClubWithCoachesResponse>> getAllClubs(Pageable pageable) {
        return ResponseEntity.ok(clubService.getAllClubs(pageable));
    }

    @Operation(
            summary = "Create a new club by an admin",
            description = "Creates a new club without assigning a coach.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Club created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClubResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.ADMIN})
    @PostMapping("/admin")
    public ResponseEntity<ClubResponse> createClubByAdmin(@RequestBody @Valid ClubRequest clubRequest) {
        return ResponseEntity.ok(clubService.createClub(clubRequest));
    }

    @Operation(
            summary = "Create a new club by a coach",
            description = "Creates a new club and assigns the coach automatically.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Club created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClubWithCoachesResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint(roles = {Role.COACH})
    @PostMapping("/coach")
    public ResponseEntity<ClubWithCoachesResponse> createClubByCoach(
            @RequestBody @Valid ClubRequest clubRequest) {
        return ResponseEntity.ok(clubService.createClubWithCoach(clubRequest));
    }
}
