package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MapperUtils mapperUtils;

    public static void applyRolesToUser(User user, Role role) {
        if (user.getRoles().contains(role)) {
            log.warn("User already has role: {}", role);
            throw new IllegalArgumentException("User already has role: " + role);
        }
        user.getRoles().add(role);
    }

    public static String generatePassword() {
        int length = 12;
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=[]{}|;:,.<>?";
        SecureRandom random = new SecureRandom();
        return random.ints(length, 0, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    public User createUser(String email, String password) {
        log.info("Creating user with email: {}", email);
        AuthRequest authRequest = new AuthRequest(email, password);
        return authService.createUser(authRequest);
    }

    public UserResponse getMe(User user) {
        log.info("Getting user with email: {}", user.getEmail());
        return mapperUtils.mapToUserResponse(user);
    }

    @Transactional
    public UserResponse updateMe(User user, UserDetailsRequest userDetailsRequest) {
        log.info("Updating user with email: {}", user.getEmail());

        user.setFirstName(userDetailsRequest.firstName());
        user.setLastName(userDetailsRequest.lastName());
        user.setGender(userDetailsRequest.gender());
        user.setBirthDate(userDetailsRequest.birthDate());
        user.setNationality(userDetailsRequest.nationality());
        user.setProfilePictureUrl(userDetailsRequest.profilePictureUrl());

        User userSaved = userRepository.save(user);
        return mapperUtils.mapToUserResponse(userSaved);

    }
}
