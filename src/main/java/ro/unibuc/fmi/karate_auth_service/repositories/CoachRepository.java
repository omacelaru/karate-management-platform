package ro.unibuc.fmi.karate_auth_service.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;

import java.util.Optional;
import java.util.Set;

public interface CoachRepository extends JpaRepository<Coach, Long>, JpaSpecificationExecutor<Coach> {
    Optional<Coach> findByUserEmail(String email);

    Set<Coach> findAllByClubId(@NotNull Long clubId);
}