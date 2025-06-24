package ro.unibuc.fmi.karate_management_platform.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.strategies.matchResult.MatchResultStrategy;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchResultStrategyFactory {
    private final List<MatchResultStrategy> strategies;

    public MatchResultStrategy getStrategy(MatchResultRequest request) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for request type: " + request.getClass().getSimpleName()));
    }
} 