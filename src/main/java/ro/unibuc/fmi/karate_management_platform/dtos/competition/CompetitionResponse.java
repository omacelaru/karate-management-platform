package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import lombok.Builder;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryResponse;
import ro.unibuc.fmi.karate_management_platform.dtos.organizer.OrganizerResponse;
import ro.unibuc.fmi.karate_management_platform.models.competition.Competition;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link Competition}
 */
@Builder
public record CompetitionResponse(Long id, String name, String location, LocalDate date, boolean registrationOpen,OrganizerResponse organizer,
                                  Set<CategoryResponse> categories) implements Serializable {
}