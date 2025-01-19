package ro.unibuc.fmi.karate_management_platform.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;

import java.util.Optional;

public interface AthleteRepository extends JpaRepository<Athlete, Long>, JpaSpecificationExecutor<Athlete> {
    Optional<Athlete> findByUserEmail(String email);
}
