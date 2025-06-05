package ro.unibuc.fmi.karate_management_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.CategoryScheduling;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySchedulingRepository extends JpaRepository<CategoryScheduling, Long> {
    void deleteByCompetitionId(Long competitionId);
    List<CategoryScheduling> findAllByCompetitionId(Long competitionId);
    Optional<CategoryScheduling> findByCompetitionIdAndCategoryId(Long competitionId, Long categoryId);
} 