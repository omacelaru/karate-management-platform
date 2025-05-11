package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.ResetPasswordRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserService userService;
    private JwtTokenService jwtTokenService;
    private AuthenticationManager authenticationManager;
    private MapperUtils mapperUtils;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtTokenService = mock(JwtTokenService.class);
        authenticationManager = mock(AuthenticationManager.class);
        mapperUtils = mock(MapperUtils.class);
        authService = new AuthService(userService, jwtTokenService, authenticationManager, mapperUtils,emailService);
    }

    @Test
    void login_shouldAuthenticateAndReturnTokens() {
        AuthRequest authRequest = new AuthRequest("user@example.com", "password123");
        User user = new User();
        user.setEmail(authRequest.email());

        when(userService.findByEmail(authRequest.email())).thenReturn(Optional.of(user));
        when(jwtTokenService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(mapperUtils.mapToAuthResponse("access-token", "refresh-token"))
                .thenReturn(new AuthResponse("access-token", "refresh-token"));

        AuthResponse response = authService.login(authRequest);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByEmail(authRequest.email());
    }

    @Test
    void login_shouldThrowExceptionIfUserNotFound() {
        AuthRequest authRequest = new AuthRequest("nonexistent@example.com", "password123");

        when(userService.findByEmail(authRequest.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(authRequest))
                .isInstanceOf(RuntimeException.class);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByEmail(authRequest.email());
    }

    @Test
    void register_shouldCreateUserAndReturnTokens() {
        AuthRequest authRequest = new AuthRequest("newuser@example.com", "password123");
        User user = new User();
        user.setEmail(authRequest.email());

        when(userService.createUser(authRequest)).thenReturn(user);
        when(jwtTokenService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(mapperUtils.mapToAuthResponse("access-token", "refresh-token"))
                .thenReturn(new AuthResponse("access-token", "refresh-token"));

        AuthResponse response = authService.register(authRequest);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(userService).createUser(authRequest);
    }

    @Test
    void refreshToken_shouldReturnNewAccessToken() {
        User user = new User();
        user.setEmail("user@example.com");
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(jwtTokenService.extractRefreshToken(request)).thenReturn("refresh-token");
        when(jwtTokenService.generateAccessToken(user)).thenReturn("new-access-token");
        when(mapperUtils.mapToAuthResponse("new-access-token", "refresh-token"))
                .thenReturn(new AuthResponse("new-access-token", "refresh-token"));

        AuthResponse response = authService.refreshToken(user, request);

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(jwtTokenService).extractRefreshToken(request);
    }

    @Test
    void resetPassword_shouldUpdateUserPassword() {
        User user = new User();
        user.setEmail("user@example.com");
        ResetPasswordRequest request = new ResetPasswordRequest("old-password", "new-password");

        authService.resetPassword(user, request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).updatePassword(user, request.newPassword());
    }
}
