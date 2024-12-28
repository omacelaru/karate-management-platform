package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;

import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Long>, JpaSpecificationExecutor<Coach> {
    Optional<Coach> findByUserEmail(String email);
}