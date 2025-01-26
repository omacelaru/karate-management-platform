package ro.unibuc.fmi.karate_management_platform.dtos.competition.category;

import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ro.unibuc.fmi.karate_management_platform.models.competition.category.Category}
 */
public record CategoryIDsRequest(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, AgeGroup ageGroup,
                                 Gender gender, Boolean isDefault) implements Serializable {
}