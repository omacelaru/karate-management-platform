package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.fixtures.organizer.OrganizerResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.organizer.Organizer;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.OrganizerRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OrganizerServiceTest {

    private OrganizerService organizerService;
    private OrganizerRepository organizerRepository;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        organizerRepository = mock(OrganizerRepository.class);
        mapperUtils = mock(MapperUtils.class);
        organizerService = new OrganizerService(organizerRepository, mapperUtils);
    }

    @Test
    void getAllOrganizers_shouldReturnPageOfOrganizers() {
        Pageable pageable = mock(Pageable.class);
        Organizer organizer = new Organizer();
        OrganizerResponse response = OrganizerResponseFixture.createDefaultOrganizerResponse();

        when(organizerRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(organizer)));
        when(mapperUtils.mapToOrganizerResponse(organizer)).thenReturn(response);

        Page<OrganizerResponse> result = organizerService.getAllOrganizers(pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(organizerRepository).findAll(pageable);
    }

    @Test
    void getMe_shouldReturnOrganizer() {
        User user = new User();
        user.setEmail("organizer@example.com");
        Organizer organizer = new Organizer();
        OrganizerResponse response = OrganizerResponseFixture.createDefaultOrganizerResponse();

        when(organizerRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(organizer));
        when(mapperUtils.mapToOrganizerResponse(organizer)).thenReturn(response);

        OrganizerResponse result = organizerService.getMe(user);

        assertThat(result).isEqualTo(response);
        verify(organizerRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void getMe_shouldThrowIncompleteProfileExceptionIfOrganizerNotFound() {
        User user = new User();
        user.setEmail("nonexistent@example.com");

        when(organizerRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizerService.getMe(user))
                .isInstanceOf(IncompleteProfileException.class);

        verify(organizerRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void createOrganizer_shouldCreateAndSaveOrganizer() {
        User user = new User();
        user.setEmail("organizer@example.com");
        OrganizerRequest request = mock(OrganizerRequest.class);
        Organizer organizer = new Organizer();

        when(mapperUtils.mapToOrganizer(request)).thenReturn(organizer);

        organizerService.createOrganizer(user, request);

        ArgumentCaptor<Organizer> organizerCaptor = ArgumentCaptor.forClass(Organizer.class);
        verify(organizerRepository).save(organizerCaptor.capture());
        Organizer capturedOrganizer = organizerCaptor.getValue();

        assertThat(capturedOrganizer.getUser()).isEqualTo(user);
        verify(mapperUtils).mapToOrganizer(request);
    }
}