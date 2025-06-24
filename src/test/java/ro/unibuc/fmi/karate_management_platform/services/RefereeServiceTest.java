package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.referee.RefereeResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.referee.RefereeResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.referee.Referee;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.RefereeRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RefereeServiceTest {

    private RefereeService refereeService;
    private RefereeRepository refereeRepository;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        refereeRepository = mock(RefereeRepository.class);
        mapperUtils = mock(MapperUtils.class);
        refereeService = new RefereeService(refereeRepository, mapperUtils);
    }

    @Test
    void getAllReferees_shouldReturnPageOfReferees() {
        Pageable pageable = mock(Pageable.class);
        Referee referee = new Referee();
        RefereeResponse response = RefereeResponseFixture.createDefaultRefereeResponse();

        when(refereeRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(referee)));
        when(mapperUtils.mapToRefereeResponse(referee)).thenReturn(response);

        Page<RefereeResponse> result = refereeService.getAllReferees(pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(refereeRepository).findAll(pageable);
    }

    @Test
    void getMe_shouldReturnReferee() {
        User user = new User();
        user.setEmail("referee@example.com");
        Referee referee = new Referee();
        RefereeResponse response = RefereeResponseFixture.createDefaultRefereeResponse();

        when(refereeRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(referee));
        when(mapperUtils.mapToRefereeResponse(referee)).thenReturn(response);

        RefereeResponse result = refereeService.getMe(user);

        assertThat(result).isEqualTo(response);
        verify(refereeRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void getMe_shouldThrowExceptionIfRefereeNotFound() {
        User user = new User();
        user.setEmail("nonexistent@example.com");

        when(refereeRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refereeService.getMe(user))
                .isInstanceOf(NoSuchElementException.class);

        verify(refereeRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void createReferee_shouldCreateAndSaveReferee() {
        User user = new User();
        user.setEmail("referee@example.com");
        RefereeRequest request = mock(RefereeRequest.class);
        Referee referee = new Referee();

        when(mapperUtils.mapToReferee(request)).thenReturn(referee);

        refereeService.createReferee(user, request);

        ArgumentCaptor<Referee> refereeCaptor = ArgumentCaptor.forClass(Referee.class);
        verify(refereeRepository).save(refereeCaptor.capture());
        Referee capturedReferee = refereeCaptor.getValue();

        assertThat(capturedReferee.getUser()).isEqualTo(user);
        verify(mapperUtils).mapToReferee(request);
    }
}