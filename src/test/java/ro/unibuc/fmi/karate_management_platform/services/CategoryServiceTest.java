package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataBeltRange;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kata.KataIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteIndividualCategory;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite.KumiteDivisionRange;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.CategoryRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        MapperUtils mapperUtils = mock(MapperUtils.class);
        categoryService = new CategoryService(categoryRepository,mapperUtils);
    }

    @Test
    void getCategoriesByIds_shouldReturnAllCategoriesWhenFound() {
        // Mock data
        KataIndividualCategory kataIndividualCategory = KataIndividualCategory.builder()
                .id(1L)
                .kataBeltRange(KataBeltRange.OPEN)
                .build();

        KumiteIndividualCategory kumiteIndividualCategory = KumiteIndividualCategory.builder()
                .id(2L)
                .kumiteDivisionRange(KumiteDivisionRange.BETWEEN_65_70KG)
                .build();

        // Input IDs
        Set<Long> ids = Set.of(1L, 2L);

        // Mock repository behavior
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(kataIndividualCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(kumiteIndividualCategory));

        // Execute
        Set<Category> result = categoryService.getCategoriesByIds(ids);

        // Verify
        assertThat(result).containsExactlyInAnyOrder(kataIndividualCategory, kumiteIndividualCategory);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
    }

    @Test
    void getCategoriesByIds_shouldReturnOnlyExistingCategories() {
        // Mock data
        KataIndividualCategory kataIndividualCategory = KataIndividualCategory.builder()
                .id(1L)
                .kataBeltRange(KataBeltRange.WHITE_TO_ORANGE)
                .build();

        // Input IDs
        Set<Long> ids = Set.of(1L, 2L);

        // Mock repository behavior
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(kataIndividualCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        // Execute
        Set<Category> result = categoryService.getCategoriesByIds(ids);

        // Verify
        assertThat(result).containsExactly(kataIndividualCategory);
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
    }

    @Test
    void getCategoriesByIds_shouldReturnEmptySetWhenNoCategoriesFound() {
        // Input IDs
        Set<Long> ids = Set.of(3L, 4L);

        // Mock repository behavior
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execute
        Set<Category> result = categoryService.getCategoriesByIds(ids);

        // Verify
        assertThat(result).isEmpty();
        verify(categoryRepository).findById(3L);
        verify(categoryRepository).findById(4L);
    }
}