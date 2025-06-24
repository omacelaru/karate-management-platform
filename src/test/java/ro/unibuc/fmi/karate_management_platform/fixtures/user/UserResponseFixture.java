package ro.unibuc.fmi.karate_management_platform.fixtures.user;

import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class UserResponseFixture {
    public static UserResponse createDefaultUserResponse() {
        User user = UserFixture.createDefaultUser();
        return new UserResponse(
                user.getId(),
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now(),
                user.getRoles(),
                user.getLastName(),
                user.getFirstName(),
                user.getEmail(),
                user.getNationality(),
                user.getBirthDate(),
                user.getGender(),
                user.getProfilePictureUrl()
        );
    }

    public static UserResponse createCustomUserResponse(
            Long id,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Set<Role> roles,
            String lastName,
            String firstName,
            String email,
            String nationality,
            LocalDate birthDate,
            Gender gender,
            String profilePictureUrl) {
        return new UserResponse(id, createdAt, updatedAt, roles, lastName, firstName,
                email, nationality, birthDate, gender, profilePictureUrl);
    }


}
