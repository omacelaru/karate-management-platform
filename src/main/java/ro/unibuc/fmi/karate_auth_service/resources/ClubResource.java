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
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponseInterface;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.factories.ClubCreationStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.ClubService;

import java.util.Set;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
public class ClubResource {
    private final ClubService clubService;
    private final ClubCreationStrategyFactory clubCreationStrategyFactory;

    @Operation(summary = "Get all clubs paginated",
            description = "Get all clubs paginated with the given page and size")
    @ApiResponse(responseCode = "200", description = "Return all clubs paginated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedModel.class)))
    @GetMapping
    public ResponseEntity<Page<ClubWithCoachesResponse>> getAllClubs(Pageable pageable) {
        return ResponseEntity.ok(clubService.getAllClubs(pageable));
    }

    @Operation(
            summary = "Create a new club",
            description = "Creates a new club without assigning a coach for admin, or assigns a coach for coach.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Club created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClubResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @PostMapping()
    @SecuredEndpoint(roles = {Role.ADMIN, Role.COACH})
    public ResponseEntity<ClubResponseInterface> createClub(@RequestBody @Valid ClubRequest clubRequest, @AuthenticationPrincipal User user) {
        Set<Role> roles = user.getRoles();
        Function<ClubRequest, ClubResponseInterface> strategy = clubCreationStrategyFactory.getStrategy(roles);
        return ResponseEntity.ok(strategy.apply(clubRequest));
    }
    //todo de exemplu, Location: /clubs/{id}). Acesta este un detaliu important care este adesea folosit în REST pentru a permite clientului să acceseze imediat resursa creată.


}
