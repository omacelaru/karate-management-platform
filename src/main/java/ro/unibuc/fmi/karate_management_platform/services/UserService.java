package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperUtils mapperUtils;

    public Optional<User> findByEmail(String email) {
        log.info("Finding user with email: {}", email);
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll();
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

    @Transactional
    public User createUser(AuthRequest authRequest) {
        return createUser(authRequest.email(), authRequest.password());
    }

    @Transactional
    public User createUser(String email, String password) {
        log.info("Creating user with email: {}", email);
        if (userRepository.existsByEmail(email)) {
            log.error("Email already exists");
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .roles(new HashSet<>(Set.of(Role.USER)))
                .build();
        return userRepository.save(user);
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

    public void updatePassword(User user, String password) {
        log.info("Updating password for user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
