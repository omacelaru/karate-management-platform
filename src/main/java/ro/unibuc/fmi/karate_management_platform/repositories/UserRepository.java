package ro.unibuc.fmi.karate_management_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import ro.unibuc.fmi.karate_management_platform.models.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(@NonNull String email);
    boolean existsByEmail(@NonNull String email);
}