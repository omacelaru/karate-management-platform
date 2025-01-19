package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_management_platform.models.club.Club;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.user.User;
import ro.unibuc.fmi.karate_management_platform.repositories.ClubRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.CoachRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final CoachRepository coachRepository;
    private final MapperUtils mapperUtils;

    public Page<ClubWithCoachesResponse> getAllClubs(Pageable pageable) {
        log.info("Getting all clubs");
        return clubRepository.findAll(pageable).map(mapperUtils::mapToClubWithCoachesResponse);
    }

    @Transactional
    public ClubResponse createClubByAdmin(User user, @Valid ClubRequest clubRequest) {
        log.info("Creating club {} by user-admin {}", clubRequest, user);
        validateClubRequest(clubRequest);

        Club club = mapperUtils.mapToClub(clubRequest);

        Club clubSaved = clubRepository.save(club);
        return mapperUtils.mapToClubResponse(clubSaved);
    }

    @Transactional
    public ClubWithCoachesResponse createClubByCoach(User user, @Valid ClubRequest clubRequest) {
        log.info("Creating club {} by user-coach {}", clubRequest, user);
        validateClubRequest(clubRequest);

        Coach coach = coachRepository.findByUserEmail(user.getEmail()).orElseThrow();
        Optional<Club> clubOptional = clubRepository.findByCoachesUserEmail(user.getEmail());
        if (clubOptional.isPresent()) {
            log.error("Coach already has a club");
            throw new IllegalArgumentException("Coach already has a club");
        }

        Club club = mapperUtils.mapToClub(clubRequest);
        coach.setClub(club);
        club.setCoaches(Set.of(coach));

        Club clubSaved = clubRepository.save(club);
        return mapperUtils.mapToClubWithCoachesResponse(clubSaved);
    }

    private void validateClubRequest(ClubRequest clubRequest) {
        if (clubRepository.existsClubByName(clubRequest.name())) {
            log.error("Club name already exists");
            throw new IllegalArgumentException("Club name already exists");
        }
        if (clubRepository.existsClubByAcronym(clubRequest.acronym())) {
            log.error("Club acronym already exists");
            throw new IllegalArgumentException("Club acronym already exists");
        }
    }

    public Set<Coach> getCoachesForClub(@NotNull Long clubId) {
        log.info("Getting coaches for club with id: {}", clubId);
        return coachRepository.findAllByClubId(clubId);
    }

    public Club getClub(@NotNull(message = "Club ID must not be null") Long clubId) {
        log.info("Getting club with id: {}", clubId);
        return clubRepository.findById(clubId).orElseThrow(() -> new IllegalArgumentException("Club not found"));
    }
}
