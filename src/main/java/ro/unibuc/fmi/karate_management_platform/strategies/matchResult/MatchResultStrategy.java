package ro.unibuc.fmi.karate_management_platform.strategies.matchResult;

import ro.unibuc.fmi.karate_management_platform.dtos.competition.match.request.MatchResultRequest;
import ro.unibuc.fmi.karate_management_platform.models.competition.match.Match;

public interface MatchResultStrategy {
    Match processMatchResult(MatchResultRequest request);
    boolean supports(MatchResultRequest request);
} 