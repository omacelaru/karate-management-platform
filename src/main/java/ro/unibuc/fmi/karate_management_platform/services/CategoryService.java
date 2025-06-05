package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kata.KataTeamCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite.KumiteTeamCategory;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.CategoryRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository<? extends Category> categoryRepository;
    private final MapperUtils mapperUtils;

    public Set<Category> getCategoriesByIds(Set<Long> longs) {
        log.info("Getting categories by ids {}", longs);

        return longs.stream()
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Set<Category> getDefaultCategoryEntities() {
        return categoryRepository.findAll().stream()
                .filter(Category::getIsDefault)
                .collect(Collectors.toSet());
    }

    public Set<CategoryResponse> getDefaultCategoryResponses() {
        return categoryRepository.findAll().stream()
                .filter(Category::getIsDefault)
                .map(this::mapToCategoryResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        switch (category) {
            case KataIndividualCategory kataCategory -> {
                return mapperUtils.mapToKataIndividualCategoryResponse(kataCategory);
            }
            case KumiteIndividualCategory kumiteCategory -> {
                return mapperUtils.mapToKumiteIndividualCategoryResponse(kumiteCategory);
            }
            case KataTeamCategory kataCategory -> {
                return mapperUtils.mapToKataTeamCategoryResponse(kataCategory);
            }
            case KumiteTeamCategory kumiteCategory -> {
                return mapperUtils.mapToKumiteTeamCategoryResponse(kumiteCategory);
            }
            default -> {
                log.warn("Unknown category type: {}", category.getClass().getSimpleName());
                return null;
            }
        }
    }

    public Category getCategoryById(Long categoryId) {
        log.info("Getting category by id {}", categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with id " + categoryId + " not found"));
    }
}