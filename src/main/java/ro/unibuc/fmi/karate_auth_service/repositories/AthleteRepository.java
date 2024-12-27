package ro.unibuc.fmi.karate_auth_service.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_auth_service.models.athelte.Athlete;

import java.util.Optional;

public interface AthleteRepository extends JpaRepository<Athlete, Long>, JpaSpecificationExecutor<Athlete> {
    Optional<Athlete> findByUserEmail(String email);
}
