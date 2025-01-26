package ro.unibuc.fmi.karate_management_platform.repositories.competition.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;

public interface MatchRepository<T extends Match> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}