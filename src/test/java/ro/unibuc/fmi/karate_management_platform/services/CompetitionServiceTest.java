package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.CompetitionResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.CompetitionFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.CompetitionRequestFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.competition.category.CategoryFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.organizer.OrganizerFixture;
import ro.unibuc.fmi.karate_management_platform.manager.AthleteCategoryRegistrationManager;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.CompetitionRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private MapperUtils mapperUtils;
    @Mock
    private OrganizerRepository organizerRepository;
    @Mock
    private AthleteCategoryRegistrationManager athleteCategoryRegistrationManager;
    @Mock
    private CoachService coachService;

    @InjectMocks
    private CompetitionService competitionService;

    @Test
    void createCompetition_shouldSaveCompetitionSuccessfully() {
        User user = new User();
        user.setEmail("organizer@example.com");
        CompetitionRequest request = CompetitionRequestFixture.createDefaultCompetitionRequest();
        Organizer organizer = OrganizerFixture.createDefaultOrganizer();
        Set<Category> categories = Set.of(CategoryFixture.createDefaultCategory());
        Competition competition = CompetitionFixture.createDefaultCompetition();

        when(organizerRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(organizer));
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

    @Test
    void testGetAllCompetitions_success() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        Competition competition1 = new Competition();
        Competition competition2 = new Competition();
        List<Competition> competitions = List.of(competition1, competition2);
        Page<Competition> competitionPage = new PageImpl<>(competitions);
        when(competitionRepository.findAll(pageable))
                .thenReturn(competitionPage);

        CompetitionResponse response1 = CompetitionResponse.builder().id(1L).build();
        CompetitionResponse response2 = CompetitionResponse.builder().id(2L).build();
        when(mapperUtils.mapToCompetitionResponse(competition1))
                .thenReturn(response1);
        when(mapperUtils.mapToCompetitionResponse(competition2))
                .thenReturn(response2);

        // Act
        Page<CompetitionResponse> result = competitionService.getAllCompetitions(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).id());
        assertEquals(2L, result.getContent().get(1).id());
    }

    @Test
    void testGetCompetitionById_success() {
        // Arrange
        Long competitionId = 1L;
        Competition competition = new Competition();
        competition.setId(competitionId);
        when(competitionRepository.findById(competitionId))
                .thenReturn(Optional.of(competition));

        CompetitionResponse response = CompetitionResponse.builder()
                .id(competitionId)
                .build();
        when(mapperUtils.mapToCompetitionResponse(competition))
                .thenReturn(response);

        // Act
        CompetitionResponse result = competitionService.getCompetitionById(competitionId);

        // Assert
        assertNotNull(result);
        assertEquals(competitionId, result.id());
    }

    @Test
    void testGetCompetitionById_notFound() {
        // Arrange
        Long competitionId = 1L;
        when(competitionRepository.findById(competitionId))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> competitionService.getCompetitionById(competitionId));
        assertEquals("Competition not found", exception.getMessage());
    }
}