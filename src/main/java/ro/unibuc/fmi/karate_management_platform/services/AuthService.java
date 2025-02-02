package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.ResetPasswordRequest;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final MapperUtils mapperUtils;

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        log.info("Logging in user with email: {}", authRequest.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password()));
        User user = userService.findByEmail(authRequest.email()).orElseThrow();
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse register(AuthRequest authRequest) {
        log.info("Registering user with email: {}", authRequest.email());
        User user = userService.createUser(authRequest);
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(User user, HttpServletRequest request) {
        log.info("Refreshing token for user with email: {}", user.getEmail());
        String refreshToken = jwtTokenService.extractRefreshToken(request);
        String accessToken = jwtTokenService.generateAccessToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }

    public Void resetPassword(User user, ResetPasswordRequest request) {
        log.info("Resetting password for user with email: {}", user.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), request.oldPassword()));

        userService.updatePassword(user, request.newPassword());

        return null;
    }
}
