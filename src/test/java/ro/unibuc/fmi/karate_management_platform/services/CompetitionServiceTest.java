package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.CompetitionFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.CompetitionRequestFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.category.CategoryFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.organizer.OrganizerFixture;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CompetitionServiceTest {

    private CompetitionService competitionService;
    private CompetitionRepository competitionRepository;
    private CategoryService categoryService;
    private MapperUtils mapperUtils;
    private OrganizerRepository organizerRepository;

    @BeforeEach
    void setUp() {
        competitionRepository = mock(CompetitionRepository.class);
        categoryService = mock(CategoryService.class);
        mapperUtils = mock(MapperUtils.class);
        organizerRepository = mock(OrganizerRepository.class);
        competitionService = new CompetitionService(competitionRepository, categoryService, mapperUtils, organizerRepository);
    }

    @Test
    void createCompetition_shouldSaveCompetitionSuccessfully() {
        User user = new User();
        user.setEmail("organizer@example.com");
        CompetitionRequest request = CompetitionRequestFixture.createDefaultCompetitionRequest();
        Organizer organizer = OrganizerFixture.createDefaultOrganizer();
        Set<Category> categories = Set.of(CategoryFixture.createDefaultCategory());
        Competition competition = CompetitionFixture.createDefaultCompetition();

        when(organizerRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(organizer));
        when(categoryService.getCategoriesByIds(request.categoriesIds())).thenReturn(categories);
        when(mapperUtils.mapToCompetition(request)).thenReturn(competition);

        competitionService.createCompetition(user, request);

        ArgumentCaptor<Competition> competitionCaptor = ArgumentCaptor.forClass(Competition.class);
        verify(competitionRepository).save(competitionCaptor.capture());
        Competition capturedCompetition = competitionCaptor.getValue();

        assertThat(capturedCompetition.getName()).isEqualTo(request.name());
        assertThat(capturedCompetition.getCategories()).isEqualTo(categories);
        assertThat(capturedCompetition.getOrganizer()).isEqualTo(organizer);
    }

    @Test
    void createCompetition_shouldThrowExceptionWhenOrganizerNotFound() {
        User user = new User();
        user.setEmail("organizer@example.com");
        CompetitionRequest request = CompetitionRequestFixture.createDefaultCompetitionRequest();

        when(organizerRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> competitionService.createCompetition(user, request))
                .isInstanceOf(NoSuchElementException.class);

        verify(organizerRepository).findByUserEmail(user.getEmail());
        verifyNoInteractions(competitionRepository, categoryService, mapperUtils);
    }
}