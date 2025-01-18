package ro.unibuc.fmi.karate_auth_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_auth_service.models.organizer.Organizer;

public interface OrganizerRepository extends JpaRepository<Organizer, Long>, JpaSpecificationExecutor<Organizer> {
}