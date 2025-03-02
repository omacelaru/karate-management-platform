package ro.unibuc.fmi.karate_management_platform.dtos.competition.registration;

import jakarta.validation.Valid;

import java.util.Set;

public record RegistrationRequest(@Valid Set<AthleteCompetitionRegistration> athletes) {
}
