package ro.unibuc.fmi.karate_auth_service.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.exceptions.ApiError;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_auth_service.services.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @Operation(summary = "Get logged in user information",
            description = "Returns information about the logged in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information returned successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.getMe(user));
    }

    @Operation(summary = "Update logged in user information",
            description = "Added the rest of the user details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User information updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UserDetailsRequest userDetailsRequest) {
        return ResponseEntity.ok(userService.updateMe(user, userDetailsRequest));
    }

}
