package ro.unibuc.fmi.karate_management_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;

import java.util.Optional;
import java.util.Set;

public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {
    boolean existsByAthletes_IdIn(Set<Long> athletesIds);

    Optional<Team> findByAthletes_IdIn(Set<Long> athletesIds);
}