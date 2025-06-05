package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;
import ro.unibuc.fmi.karate_management_platform.repositories.competition.match.MatchRepository;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    @Transactional
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return matchRepository.existsById(id);
    }
} 