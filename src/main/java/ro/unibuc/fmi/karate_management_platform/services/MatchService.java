package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response.MatchResponse;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.match.MatchRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MapperUtils mapperUtils;

    @Transactional
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return matchRepository.existsById(id);
    }

    public Optional<MatchResponse> findByCompetitionIdAndCategoryId(Long competitionId, Long categoryId) {
        Optional<Match> match = matchRepository.findByCompetitionIdAndCategoryId(competitionId, categoryId);
        return match.map(mapperUtils::mapMatch);
    }

    public List<Match> findAllByCompetitionId(Long competitionId) {
        return matchRepository.findAllByCompetitionId(competitionId);
    }

    @Data
    @AllArgsConstructor
    public static class PodiumResult {
        private ClubResponse club;
        private int place;
    }

    public List<PodiumResult> getPodiumForMatch(Match match) {
        // This logic assumes that each match has a way to get participants and their points/scores
        // and that each participant has a club. Adjust as needed for your model.
        // Example for individual matches:
        List<Object[]> podium = new java.util.ArrayList<>();
        if (match instanceof ro.unibuc.fmi.karate_management_platform.models.competition.match.IndividualKataMatch kataMatch) {
            podium = kataMatch.getAthleteScores().entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .map(e -> new Object[]{mapperUtils.mapToClubResponse(e.getKey().getClub()), 0})
                .collect(Collectors.toList());
        }
        // TODO: Add logic for other match types (Kumite, Team, etc.)
        // After sorting, assign place 1,2,3
        for (int i = 0; i < podium.size(); i++) {
            podium.get(i)[1] = i + 1;
        }
        return podium.stream()
            .map(arr -> new PodiumResult((ClubResponse) arr[0], (int) arr[1]))
            .collect(Collectors.toList());
    }
} 