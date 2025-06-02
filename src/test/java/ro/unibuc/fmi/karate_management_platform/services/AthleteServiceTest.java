package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.user.UserResponse;
import ro.unibuc.fmi.karate_management_platform.exceptions.IncompleteProfileException;
import ro.unibuc.fmi.karate_management_platform.fixtures.club.ClubResponseFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.user.UserResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Belt;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.AthleteRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.TeamRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AthleteServiceTest {

    private AthleteRepository athleteRepository;
    private MapperUtils mapperUtils;
    private ClubService clubService;
    private AthleteService athleteService;

    private static final String EMAIL = "test@example.com";
    private static final String NOT_FOUND_EMAIL = "notfound@example.com";
    private static final Long CLUB_ID = 1L;
    private static final Integer HEIGHT = 175;
    private static final Integer WEIGHT = 80;

    @BeforeEach
    void setUp() {
        athleteRepository = mock(AthleteRepository.class);
        mapperUtils = mock(MapperUtils.class);
        clubService = mock(ClubService.class);
        TeamRepository teamRepository = mock(TeamRepository.class);
        athleteService = new AthleteService(athleteRepository,teamRepository, mapperUtils, clubService);
    }

    @Test
    void getAllAthletes_shouldReturnPageOfAthletes() {
        Pageable pageable = mock(Pageable.class);

        UserResponse userResponse = UserResponseFixture.createDefaultUserResponse();
        ClubResponse clubResponse = ClubResponseFixture.createDefaultClubResponse();
        Athlete athlete = new Athlete();
        AthleteResponse athleteResponse = new AthleteResponse(userResponse, HEIGHT, WEIGHT, Belt.BLACK, Optional.of(clubResponse));

        when(athleteRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(athlete)));
        when(mapperUtils.mapToAthleteResponse(athlete)).thenReturn(athleteResponse);

        Page<AthleteResponse> result = athleteService.getAllAthletes(pageable);

        assertThat(result.getContent()).containsExactly(athleteResponse);
        verify(athleteRepository).findAll(pageable);
    }

    @Test
    void getMe_shouldReturnAthlete() throws IncompleteProfileException {
        UserResponse userResponse = UserResponseFixture.createDefaultUserResponse();
        ClubResponse clubResponse = ClubResponseFixture.createDefaultClubResponse();
        User user = new User();
        user.setEmail(userResponse.email());
        Athlete athlete = new Athlete();
        AthleteResponse athleteResponse = new AthleteResponse(userResponse, HEIGHT, WEIGHT,Belt.BLACK, Optional.of(clubResponse));

        when(athleteRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(athlete));
        when(mapperUtils.mapToAthleteResponse(athlete)).thenReturn(athleteResponse);

        AthleteResponse result = athleteService.getMe(user);

        assertThat(result).isEqualTo(athleteResponse);
        verify(athleteRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void getMe_shouldThrowExceptionIfAthleteNotFound() {
        User user = new User();
        user.setEmail(NOT_FOUND_EMAIL);

        when(athleteRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> athleteService.getMe(user))
                .isInstanceOf(IncompleteProfileException.class);

        verify(athleteRepository).findByUserEmail(user.getEmail());
    }

    @Test
    void createAthlete_shouldSaveNewAthlete() {
        User user = new User();
        user.setEmail(EMAIL);
        AthleteRequest athleteRequest = mock(AthleteRequest.class);
        Athlete athlete = new Athlete();
        athlete.setUser(user);
        Set<Coach> coaches = Set.of(new Coach());

        when(mapperUtils.mapToAthlete(athleteRequest)).thenReturn(athlete);
        when(athleteRequest.clubId()).thenReturn(CLUB_ID);
        when(clubService.getCoachesForClub(CLUB_ID)).thenReturn(coaches);

        athleteService.createAthlete(user, athleteRequest);

        assertThat(athlete.getUser()).isEqualTo(user);
        assertThat(athlete.getCoaches()).isEqualTo(coaches);
        verify(athleteRepository).save(athlete);
    }
}