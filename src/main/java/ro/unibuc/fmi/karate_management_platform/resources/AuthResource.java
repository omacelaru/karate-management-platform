package ro.unibuc.fmi.karate_management_platform.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.ResetPasswordRequest;
import ro.unibuc.fmi.karate_management_platform.exceptions.ApiError;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.security.SecuredEndpoint;
import ro.unibuc.fmi.karate_management_platform.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService authService;

    @Operation(summary = "Register",
            description = "Registers a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @PostMapping("/register")
    //TODO Uncomment @Valid annotation after testing
    //TODO Throw not specific exception in case of email already exists
    public ResponseEntity<AuthResponse> register(@RequestBody /*@Valid*/ AuthRequest authRequest) {
        return ResponseEntity.ok(authService.register(authRequest));
    }

    @Operation(summary = "Login",
            description = "Logs in a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Bad credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }


    @Operation(summary = "Refresh token",
            description = "Refresh the access token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @AuthenticationPrincipal User user,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(authService.refreshToken(user, request));
    }

    @Operation(summary = "Reset password",
            description = "Resets the password using the token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password reset successfully", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
            })
    @SecuredEndpoint
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @AuthenticationPrincipal User user,
            @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(authService.resetPassword(user, request));
    }

}
