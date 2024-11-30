package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.UserRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;
    private final MapperUtils mapperUtils;

    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        User user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse register(AuthRequest authRequest) {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(HttpServletRequest request) {
        String refreshToken = jwtTokenService.extractRefreshToken(request);
        String userEmail = jwtTokenService.extractUserEmailFromRefreshToken(refreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        String accessToken = jwtTokenService.generateAccessToken(user);
        return mapperUtils.mapToAuthResponse(accessToken, refreshToken);
    }
}
