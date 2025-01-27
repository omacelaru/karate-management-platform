package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ro.unibuc.fmi.karate_management_platform.dtos.auth.AuthRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserDetailsRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.UserRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;
    private AuthService authService;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = mock(AuthService.class);
        mapperUtils = mock(MapperUtils.class);
        userService = new UserService(userRepository, authService, mapperUtils);
    }

    @Test
    void applyRolesToUser_shouldThrowExceptionIfRoleAlreadyExists() {
        User user = new User();
        user.setRoles(Set.of(Role.ADMIN));

        assertThatThrownBy(() -> UserService.applyRolesToUser(user, Role.ADMIN))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already has role: ADMIN");
    }

    @Test
    void generatePassword_shouldGenerateValidPassword() {
        String password = UserService.generatePassword();

        assertThat(password).isNotNull();
        assertThat(password.length()).isEqualTo(12);
    }

    @Test
    void createUser_shouldCallAuthService() {
        String email = "user@example.com";
        String password = "password123";
        User user = new User();

        when(authService.createUser(any(AuthRequest.class))).thenReturn(user);

        User result = userService.createUser(email, password);

        ArgumentCaptor<AuthRequest> authRequestCaptor = ArgumentCaptor.forClass(AuthRequest.class);
        verify(authService).createUser(authRequestCaptor.capture());

        AuthRequest capturedAuthRequest = authRequestCaptor.getValue();
        assertThat(capturedAuthRequest.email()).isEqualTo(email);
        assertThat(capturedAuthRequest.password()).isEqualTo(password);
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
}