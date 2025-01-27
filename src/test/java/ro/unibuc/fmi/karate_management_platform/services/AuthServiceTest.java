package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.ResetPasswordRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenService jwtTokenService;
    private AuthenticationManager authenticationManager;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenService = mock(JwtTokenService.class);
        authenticationManager = mock(AuthenticationManager.class);
        mapperUtils = mock(MapperUtils.class);
        authService = new AuthService(userRepository, passwordEncoder, jwtTokenService, authenticationManager, mapperUtils);
    }

    @Test
    void login_shouldReturnAuthResponse() {
        AuthRequest authRequest = new AuthRequest("test@example.com", "password123");
        User user = User.builder().email(authRequest.email()).build();

        when(userRepository.findByEmail(authRequest.email())).thenReturn(Optional.of(user));
        when(jwtTokenService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(mapperUtils.mapToAuthResponse("access-token", "refresh-token"))
                .thenReturn(new AuthResponse("access-token", "refresh-token"));

        AuthResponse response = authService.login(authRequest);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(authRequest.email());
    }

    @Test
    void register_shouldReturnAuthResponse() {
        AuthRequest authRequest = new AuthRequest("new@example.com", "password123");
        User user = User.builder().email(authRequest.email()).roles(Set.of(Role.USER)).build();

        when(userRepository.existsByEmail(authRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(authRequest.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtTokenService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(mapperUtils.mapToAuthResponse("access-token", "refresh-token"))
                .thenReturn(new AuthResponse("access-token", "refresh-token"));

        AuthResponse response = authService.register(authRequest);

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowExceptionIfEmailExists() {
        AuthRequest authRequest = new AuthRequest("duplicate@example.com", "password123");

        when(userRepository.existsByEmail(authRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(authRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void refreshToken_shouldReturnAuthResponse() {
        User user = User.builder().email("test@example.com").build();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(jwtTokenService.extractRefreshToken(request)).thenReturn("refresh-token");
        when(jwtTokenService.generateAccessToken(user)).thenReturn("new-access-token");
        when(mapperUtils.mapToAuthResponse("new-access-token", "refresh-token"))
                .thenReturn(new AuthResponse("new-access-token", "refresh-token"));

        AuthResponse response = authService.refreshToken(user, request);

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void resetPassword_shouldResetPasswordSuccessfully() {
        User user = User.builder().email("test@example.com").password("old-password").build();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest("old-password", "new-password");

        when(passwordEncoder.encode(resetPasswordRequest.newPassword())).thenReturn("encoded-new-password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        authService.resetPassword(user, resetPasswordRequest);

        assertThat(user.getPassword()).isEqualTo("encoded-new-password");
        verify(userRepository).save(user);
    }
}