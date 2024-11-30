package ro.unibuc.fmi.karate_auth_service.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication request")
public class AuthResponse {
    @Schema(description = "JWT access token")
    private String accessToken;
    @Schema(description = "JWT refresh token")
    private String refreshToken;
}
