package ro.unibuc.fmi.karate_auth_service.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubRequest;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubResponse;
import ro.unibuc.fmi.karate_auth_service.dtos.club.ClubWithCoachesResponse;
import ro.unibuc.fmi.karate_auth_service.models.club.Club;
import ro.unibuc.fmi.karate_auth_service.models.coach.Coach;
import ro.unibuc.fmi.karate_auth_service.models.user.User;
import ro.unibuc.fmi.karate_auth_service.repositories.ClubRepository;
import ro.unibuc.fmi.karate_auth_service.repositories.CoachRepository;
import ro.unibuc.fmi.karate_auth_service.utils.MapperUtils;
import ro.unibuc.fmi.karate_auth_service.utils.UserUtils;

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
    public ClubResponse createClub(@Valid ClubRequest clubRequest) {
        User user = UserUtils.getCurrentUser();
        validateClubRequest(clubRequest);
        log.info("Creating club {} by user-admin {}", clubRequest, user);
        Club club = mapperUtils.mapToClub(clubRequest);
        Club clubSaved = clubRepository.save(club);
        return mapperUtils.mapToClubResponse(clubSaved);
    }

    @Transactional
    public ClubWithCoachesResponse createClubWithCoach(@Valid ClubRequest clubRequest) {
        User user = UserUtils.getCurrentUser();

        validateClubRequest(clubRequest);

        Coach coach = coachRepository.findByUserEmail(user.getEmail()).orElseThrow();
        Optional<Club> clubOptional = clubRepository.findByCoachesUserEmail(user.getEmail());
        if (clubOptional.isPresent()) {
            throw new IllegalArgumentException("Coach already has a club");
        }

        log.info("Creating club {} with coach {}", clubRequest, coach);
        Club club = mapperUtils.mapToClub(clubRequest);
        coach.setClub(club);
        club.setCoaches(Set.of(coach));

        Club clubSaved = clubRepository.save(club);
        return mapperUtils.mapToClubWithCoachesResponse(clubSaved);
    }

    private void validateClubRequest(ClubRequest clubRequest) {
        if (clubRepository.existsClubByName(clubRequest.name())) {
            throw new IllegalArgumentException("Club name already exists");
        }
        if (clubRepository.existsClubByAcronym(clubRequest.acronym())) {
            throw new IllegalArgumentException("Club acronym already exists");
        }
    }
}
