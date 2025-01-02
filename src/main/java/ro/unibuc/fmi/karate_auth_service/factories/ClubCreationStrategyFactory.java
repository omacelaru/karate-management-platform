package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponseInterface;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.services.ClubService;

import java.util.Set;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class ClubCreationStrategyFactory {
    private final ClubService clubService;

    public BiFunction<User, ClubRequest, ClubResponseInterface> getStrategy(Set<Role> roles) {
        if (roles.contains(Role.ADMIN)) {
            return clubService::createClubByAdmin;
        } else if (roles.contains(Role.COACH)) {
            return clubService::createClubByCoach;
        } else {
            throw new IllegalArgumentException("User does not have the necessary roles to create a club");
        }
    }
}
