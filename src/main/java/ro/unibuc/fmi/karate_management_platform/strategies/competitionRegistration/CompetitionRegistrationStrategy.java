package ro.unibuc.fmi.karate_management_platform.strategies.competitionRegistration;

import jakarta.validation.constraints.NotNull;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;

public interface CompetitionRegistrationStrategy {
    void register(@NotNull Long id, Competition competition);
}
