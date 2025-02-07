package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        mapperUtils = mock(MapperUtils.class);
        userService = new UserService(userRepository, passwordEncoder, mapperUtils);
    }

    @Test
    void findByEmail_shouldReturnUserIfFound() {
        String email = "user@example.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).contains(user);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_shouldReturnEmptyIfNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.getAllUsers();

        assertThat(result).containsExactly(user1, user2);
        verify(userRepository).findAll();
    }

    @Test
    void generatePassword_shouldGenerateValidPassword() {
        String password = UserService.generatePassword();

        assertThat(password).isNotNull();
        assertThat(password.length()).isEqualTo(12);
    }

    @Test
    void createUser_shouldThrowExceptionIfEmailExists() {
        String email = "user@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(email, "password123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userRepository).existsByEmail(email);
    }

    @Test
    void createUser_shouldSaveUserIfEmailDoesNotExist() {
        String email = "newuser@example.com";
        String password = "password123";
        User user = new User();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser(email, password);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(email);
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(result).isEqualTo(user);
    }

    @Test
    void getMe_shouldReturnMappedUserResponse() {
        User user = new User();
        user.setEmail("user@example.com");
        UserResponse response = UserResponseFixture.createDefaultUserResponse();

        when(mapperUtils.mapToUserResponse(user)).thenReturn(response);

        UserResponse result = userService.getMe(user);

        assertThat(result).isEqualTo(response);
        verify(mapperUtils).mapToUserResponse(user);
    }

    @Test
    void updateMe_shouldUpdateAndSaveUser() {
        User user = new User();
        user.setEmail("user@example.com");
        UserDetailsRequest userDetailsRequest = mock(UserDetailsRequest.class);

        when(userDetailsRequest.firstName()).thenReturn("John");
        when(userDetailsRequest.lastName()).thenReturn("Doe");
        when(userDetailsRequest.gender()).thenReturn(null);
        when(userDetailsRequest.birthDate()).thenReturn(null);
        when(userDetailsRequest.nationality()).thenReturn("Romania");
        when(userDetailsRequest.profilePictureUrl()).thenReturn("http://example.com/picture.jpg");

        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);

        UserResponse response = UserResponseFixture.createDefaultUserResponse();
        when(mapperUtils.mapToUserResponse(savedUser)).thenReturn(response);

        UserResponse result = userService.updateMe(user, userDetailsRequest);

        assertThat(result).isEqualTo(response);
        verify(userRepository).save(user);
        verify(mapperUtils).mapToUserResponse(savedUser);
    }

    @Test
    void updatePassword_shouldUpdatePassword() {
        User user = new User();
        user.setEmail("user@example.com");
        String password = "newpassword";

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        userService.updatePassword(user, password);

        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);
    }
}
