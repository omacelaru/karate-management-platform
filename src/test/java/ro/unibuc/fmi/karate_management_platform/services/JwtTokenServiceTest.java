package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.Base64;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenServiceTest {
    private JwtTokenService jwtTokenService;

    private final static String SECRET_SIGNING_KEY = "my-very;secret-signing-key-for-jwt-tokens";

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService();
        jwtTokenService.setSecretSigningKey(Base64.getEncoder().encodeToString(SECRET_SIGNING_KEY.getBytes()));
        jwtTokenService.setAccessTokenExpiration(600000L);
        jwtTokenService.setRefreshTokenExpiration(7200000L);
    }

    @Test
    void generateAccessToken_shouldReturnValidToken() {
        UserDetails userDetails = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();

        String token = jwtTokenService.generateAccessToken(userDetails);

        assertThat(token).isNotEmpty();
        String extractedEmail = jwtTokenService.extractUserEmail(token);
        assertThat(extractedEmail).isEqualTo(userDetails.getUsername());
    }

    @Test
    void generateRefreshToken_shouldReturnValidToken() {
        UserDetails userDetails = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();

        String token = jwtTokenService.generateRefreshToken(userDetails);

        assertThat(token).isNotEmpty();
        String extractedEmail = jwtTokenService.extractUserEmail(token);
        assertThat(extractedEmail).isEqualTo(userDetails.getUsername());
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        UserDetails userDetails = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();

        String token = jwtTokenService.generateAccessToken(userDetails);

        boolean isValid = jwtTokenService.isTokenValid(token, userDetails);
        assertThat(isValid).isTrue();
    }

    @Test
    void extractUserEmail_shouldReturnEmailFromToken() {
        UserDetails userDetails = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .build();

        String token = jwtTokenService.generateAccessToken(userDetails);
        String email = jwtTokenService.extractUserEmail(token);

        assertThat(email).isEqualTo(userDetails.getUsername());
    }

    @Test
    void extractRefreshToken_shouldReturnTokenFromRequestHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "Bearer valid-refresh-token";

        when(request.getHeader("Authorization")).thenReturn(token);

        String extractedToken = jwtTokenService.extractRefreshToken(request);

        assertThat(extractedToken).isEqualTo("valid-refresh-token");
    }
}