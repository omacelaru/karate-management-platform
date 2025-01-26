package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import io.micrometer.common.lang.NonNullApi;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

import java.util.Optional;

@NonNullApi
public interface CategoryRepository<T extends Category> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    Optional<T> findById(@NotNull Long id);
}