package ro.unibuc.fmi.karate_management_platform.fixtures.user;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserFixture {

    public static User createDefaultUser() {
        return User.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .email("john.doe@example.com")
                .password("password123")
                .nationality("Romania")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .profilePictureUrl("https://example.com/profile.jpg")
                .roles(new LinkedHashSet<>(Set.of(Role.USER)))
                .build();
    }

    public static User createCustomUser(
            Long id,
            String lastName,
            String firstName,
            String email,
            String password,
            String nationality,
            LocalDate birthDate,
            Gender gender,
            String profilePictureUrl,
            Set<Role> roles
    ) {
        return User.builder()
                .id(id)
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .password(password)
                .nationality(nationality)
                .birthDate(birthDate)
                .gender(gender)
                .profilePictureUrl(profilePictureUrl)
                .roles(new LinkedHashSet<>(roles))
                .build();
    }
}