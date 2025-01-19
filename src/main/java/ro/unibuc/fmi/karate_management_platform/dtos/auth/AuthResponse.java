package ro.unibuc.fmi.karate_management_platform.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for authentication response containing JWT tokens.
 * This DTO is used to send the response with JWT access and refresh tokens
 * after a successful user authentication.
 *
 * <p>Usage: This DTO is returned after the user has successfully authenticated and is issued a JWT access and refresh token.</p>
 */
@Schema(description = "Authentication response containing JWT access and refresh tokens.")
public record AuthResponse(
        @Schema(description = "JWT access token")
        String accessToken,

        @Schema(description = "JWT refresh token")
        String refreshToken
) {
}