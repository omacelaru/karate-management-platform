package ro.unibuc.fmi.karate_management_platform.repositories.competition.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.TeamKumiteMatch;

@Repository
public interface TeamKumiteMatchRepository extends JpaRepository<TeamKumiteMatch, Long>, JpaSpecificationExecutor<TeamKumiteMatch> {
} 