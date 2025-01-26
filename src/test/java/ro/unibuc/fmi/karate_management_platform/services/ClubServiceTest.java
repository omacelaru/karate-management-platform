package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_management_platform.fixtures.club.ClubResponseFixture;
import ro.unibuc.fmi.karate_management_platform.fixtures.club.ClubWithCoachesResponseFixture;
import ro.unibuc.fmi.karate_management_platform.models.club.Club;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.ClubRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.CoachRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ClubServiceTest {

    private ClubService clubService;
    private ClubRepository clubRepository;
    private CoachRepository coachRepository;
    private MapperUtils mapperUtils;

    @BeforeEach
    void setUp() {
        clubRepository = mock(ClubRepository.class);
        coachRepository = mock(CoachRepository.class);
        mapperUtils = mock(MapperUtils.class);
        clubService = new ClubService(clubRepository, coachRepository, mapperUtils);
    }

    @Test
    void getAllClubs_shouldReturnPageOfClubs() {
        Pageable pageable = mock(Pageable.class);
        Club club = new Club();
        ClubWithCoachesResponse defaultResponse = ClubWithCoachesResponseFixture.createDefaultClubWithCoachesResponse();

        when(clubRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(club)));
        when(mapperUtils.mapToClubWithCoachesResponse(club)).thenReturn(defaultResponse);

        Page<ClubWithCoachesResponse> result = clubService.getAllClubs(pageable);

        assertThat(result.getContent()).containsExactly(defaultResponse);
        verify(clubRepository).findAll(pageable);
    }

    @Test
    void createClubByAdmin_shouldCreateClubSuccessfully() {
        User user = new User();
        ClubRequest request = mock(ClubRequest.class);
        Club club = new Club();
        ClubResponse defaultResponse = ClubResponseFixture.createDefaultClubResponse();

        when(mapperUtils.mapToClub(request)).thenReturn(club);
        when(clubRepository.save(club)).thenReturn(club);
        when(mapperUtils.mapToClubResponse(club)).thenReturn(defaultResponse);

        ClubResponse result = clubService.createClubByAdmin(user, request);

        assertThat(result).isEqualTo(defaultResponse);
        verify(clubRepository).save(club);
    }

    @Test
    void createClubByCoach_shouldCreateClubSuccessfully() {
        User user = new User();
        user.setEmail("coach@example.com");
        ClubRequest request = mock(ClubRequest.class);
        Club club = new Club();
        Coach coach = new Coach();
        ClubWithCoachesResponse defaultResponse = ClubWithCoachesResponseFixture.createDefaultClubWithCoachesResponse();

        when(coachRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(coach));
        when(clubRepository.findByCoachesUserEmail(user.getEmail())).thenReturn(Optional.empty());
        when(mapperUtils.mapToClub(request)).thenReturn(club);
        when(clubRepository.save(club)).thenReturn(club);
        when(mapperUtils.mapToClubWithCoachesResponse(club)).thenReturn(defaultResponse);

        ClubWithCoachesResponse result = clubService.createClubByCoach(user, request);

        assertThat(result).isEqualTo(defaultResponse);
        assertThat(club.getCoaches()).contains(coach);
        verify(clubRepository).save(club);
    }

    @Test
    void createClubByCoach_shouldThrowExceptionWhenCoachHasClub() {
        User user = new User();
        user.setEmail("coach@example.com");
        ClubRequest request = mock(ClubRequest.class);

        when(coachRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(new Coach()));
        when(clubRepository.findByCoachesUserEmail(user.getEmail())).thenReturn(Optional.of(new Club()));

        assertThatThrownBy(() -> clubService.createClubByCoach(user, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Coach already has a club");
    }

    @Test
    void getCoachesForClub_shouldReturnCoaches() {
        Long clubId = 1L;
        Coach coach = new Coach();

        when(coachRepository.findAllByClubId(clubId)).thenReturn(Set.of(coach));

        Set<Coach> result = clubService.getCoachesForClub(clubId);

        assertThat(result).containsExactly(coach);
        verify(coachRepository).findAllByClubId(clubId);
    }

    @Test
    void getClub_shouldReturnClub() {
        Long clubId = 1L;
        Club club = new Club();

        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        Club result = clubService.getClub(clubId);

        assertThat(result).isEqualTo(club);
        verify(clubRepository).findById(clubId);
    }

    @Test
    void getClub_shouldThrowExceptionWhenClubNotFound() {
        Long clubId = 1L;

        when(clubRepository.findById(clubId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClub(clubId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Club not found");
    }
}