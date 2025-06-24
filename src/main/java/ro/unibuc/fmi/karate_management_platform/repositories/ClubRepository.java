package ro.unibuc.fmi.karate_management_platform.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.unibuc.fmi.karate_management_platform.models.club.Club;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {
    boolean existsClubByName(String name);

    boolean existsClubByAcronym(String acronym);

    Optional<Club> findByCoachesUserEmail(String email);

    Page<Club> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}