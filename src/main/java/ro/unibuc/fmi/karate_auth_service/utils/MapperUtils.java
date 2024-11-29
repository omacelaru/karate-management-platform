package ro.unibuc.fmi.karate_auth_service.utils;

import org.mapstruct.Mapper;
import ro.unibuc.fmi.karate_auth_service.dtos.auth.AuthResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_auth_service.models.user.User;

@Mapper(componentModel = "spring")
public interface MapperUtils {
    AuthResponse mapToAuthResponse(String token);

    UserResponse mapToUserResponse(User user);
}
