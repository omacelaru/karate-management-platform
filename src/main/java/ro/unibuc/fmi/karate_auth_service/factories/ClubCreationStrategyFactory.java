package ro.unibuc.fmi.karate_auth_service.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponseInterface;
import ro.unibuc.fmi.karate_auth_service.models.user.Role;
import ro.unibuc.fmi.karate_auth_service.services.ClubService;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ClubCreationStrategyFactory {
    private final ClubService clubService;

    public Function<ClubRequest, ClubResponseInterface> getStrategy(Role role) {
        return switch (role) {
            case ADMIN -> clubService::createClubByAdmin;
            case COACH -> clubService::createClubByCoach;
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
