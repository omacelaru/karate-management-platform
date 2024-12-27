package ro.unibuc.fmi.karate_auth_service.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response")
public record AuthResponse(
        @Schema(description = "JWT access token")
        String accessToken,

        @Schema(description = "JWT refresh token")
        String refreshToken
) {
}