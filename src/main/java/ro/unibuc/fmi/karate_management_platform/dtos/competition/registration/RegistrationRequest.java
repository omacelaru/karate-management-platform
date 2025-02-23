package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import java.util.Set;

public record RegistrationRequest(Set<AthleteCompetitionRegistration> athletes) {
}
