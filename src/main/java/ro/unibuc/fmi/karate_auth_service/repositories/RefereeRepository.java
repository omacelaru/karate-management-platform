package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.karate_auth_service.models.referee.Referee;

public interface RefereeRepository extends JpaRepository<Referee, Long> {
}