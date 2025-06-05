package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.dtos.club.ClubMedalsResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.response.MatchResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.coach.Coach;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.IndividualMatchResult;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.TeamMatchResult;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.category.CategoryRepository;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.match.MatchRepository;
import ro.unibuc.fmi.karate_management_platform.utils.MapperUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MapperUtils mapperUtils;
    private final ClubService clubService;

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

    public List<ClubMedalsResponse> getMedalsByClubForCompetition(Long competitionId) {
        List<Match> matches = matchRepository.findAllByCompetitionId(competitionId);
        Map<ro.unibuc.fmi.karate_management_platform.models.club.Club, int[]> clubMedals = new java.util.HashMap<>(); // [gold, silver, bronze]

        for (Match match : matches) {
            if (match instanceof IndividualMatchResult individual) {
                individual.getAthletePoints().entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(3)
                    .forEachOrdered(entry -> {
                        Athlete athlete = entry.getKey();
                        ro.unibuc.fmi.karate_management_platform.models.club.Club club = athlete.getCoaches().stream()
                            .findFirst()
                            .map(Coach::getClub)
                            .orElse(null);
                        if (club != null) {
                            int[] medals = clubMedals.computeIfAbsent(club, k -> new int[3]);
                            int place = (int) individual.getAthletePoints().entrySet().stream()
                                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                                .map(Map.Entry::getKey)
                                .toList().indexOf(athlete);
                            if (place == 0) medals[0]++;
                            else if (place == 1) medals[1]++;
                            else if (place == 2) medals[2]++;
                        }
                    });
            } else if (match instanceof TeamMatchResult teamMatch) {
                teamMatch.getTeamPoints().entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .limit(3)
                    .forEachOrdered(entry -> {
                        var team = entry.getKey();
                        Athlete athlete = team.getAthletes().stream().findFirst().orElse(null);
                        ro.unibuc.fmi.karate_management_platform.models.club.Club club = athlete != null ? athlete.getCoaches().stream()
                            .findFirst()
                            .map(Coach::getClub)
                            .orElse(null) : null;
                        if (club != null) {
                            int[] medals = clubMedals.computeIfAbsent(club, k -> new int[3]);
                            int place = (int) teamMatch.getTeamPoints().entrySet().stream()
                                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                                .map(Map.Entry::getKey)
                                .toList().indexOf(team);
                            if (place == 0) medals[0]++;
                            else if (place == 1) medals[1]++;
                            else if (place == 2) medals[2]++;
                        }
                    });
            }
        }
        return clubMedals.entrySet().stream()
            .map(entry -> new ClubMedalsResponse(
                clubService.findById(entry.getKey().getId()),
                entry.getValue()[0],
                entry.getValue()[1],
                entry.getValue()[2]
            ))
            .collect(Collectors.toList());
    }
}