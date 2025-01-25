package ro.unibuc.fmi.karate_management_platform.repositories.competition.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

public interface CategoryRepository<T extends Category> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}