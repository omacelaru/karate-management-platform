package ro.unibuc.fmi.karate_management_platform.factories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponseInterface;
import ro.unibuc.fmi.karate_management_platform.models.user.Role;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.services.ClubService;

import java.util.Set;
import java.util.function.BiFunction;

@Slf4j
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
            log.error("User does not have the necessary roles to create a club");
            throw new IllegalArgumentException("User does not have the necessary roles to create a club");
        }
    }
}
