package ro.unibuc.fmi.karate_management_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;

import java.util.Optional;

public interface OrganizerRepository extends JpaRepository<Organizer, Long>, JpaSpecificationExecutor<Organizer> {
    Optional<Organizer> findByUserEmail(String email);
}