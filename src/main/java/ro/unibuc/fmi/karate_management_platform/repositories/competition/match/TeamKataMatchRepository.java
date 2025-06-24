package ro.unibuc.fmi.karate_management_platform.repositories.competition.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.TeamKataMatch;

@Repository
public interface TeamKataMatchRepository extends JpaRepository<TeamKataMatch, Long>, JpaSpecificationExecutor<TeamKataMatch> {
} 