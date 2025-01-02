package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.UserRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;

    public static void applyRolesToUser(User user, Role role) {
        if (user.getRoles().contains(role)) {
            log.warn("User already has role: {}", role);
            throw new IllegalArgumentException("User already has role: " + role);
        }
        user.getRoles().add(role);
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
