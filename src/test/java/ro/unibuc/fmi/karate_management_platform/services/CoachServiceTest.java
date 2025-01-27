package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.coach.CoachResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.fixtures.coach.CoachResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.CoachRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CoachServiceTest {

    private CoachService coachService;
    private CoachRepository coachRepository;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        coachRepository = mock(CoachRepository.class);
        mapperUtils = mock(MapperUtils.class);
        coachService = new CoachService(coachRepository, mapperUtils);
    }

    @Test
    void getAllCoaches_shouldReturnPageOfCoaches() {
        Pageable pageable = mock(Pageable.class);
        Coach coach = new Coach();
        CoachResponse defaultResponse = CoachResponseFixture.createDefaultCoachResponse();

        when(coachRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(coach)));
        when(mapperUtils.mapToCoachResponse(coach)).thenReturn(defaultResponse);

        Page<CoachResponse> result = coachService.getAllCoaches(pageable);

        assertThat(result.getContent()).containsExactly(defaultResponse);
        verify(coachRepository).findAll(pageable);
    }

    @Test
    void getMe_shouldReturnCoach() throws IncompleteProfileException {
        User user = new User();
        user.setEmail("coach@example.com");
        Coach coach = new Coach();
        CoachResponse defaultResponse = CoachResponseFixture.createDefaultCoachResponse();

        when(coachRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(coach));
        when(mapperUtils.mapToCoachResponse(coach)).thenReturn(defaultResponse);

        CoachResponse result = coachService.getMe(user);

        assertThat(result).isEqualTo(defaultResponse);
        verify(coachRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void getMe_shouldThrowIncompleteProfileException() {
        User user = new User();
        user.setEmail("nonexistent@example.com");

        when(coachRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> coachService.getMe(user))
                .isInstanceOf(IncompleteProfileException.class);

        verify(coachRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void createCoach_shouldCreateCoachSuccessfully() {
        User user = new User();
        user.setEmail("coach@example.com");
        CoachRequest request = mock(CoachRequest.class);
        Coach coach = new Coach();

        when(mapperUtils.mapToCoach(request)).thenReturn(coach);

        coachService.createCoach(user, request);

        ArgumentCaptor<Coach> coachCaptor = ArgumentCaptor.forClass(Coach.class);
        verify(coachRepository).save(coachCaptor.capture());
        Coach capturedCoach = coachCaptor.getValue();

        assertThat(capturedCoach.getUser()).isEqualTo(user);
        verify(mapperUtils).mapToCoach(request);
    }
}