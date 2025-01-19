package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.karate_auth_service.models.referee.Referee;

import java.util.Optional;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
    Optional<Referee> findByUserEmail(String email);
}