package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link ro.unibuc.fmi.karate_management_platform.models.competition.Competition}
 */
@Embeddable
public record CompetitionRequest(@NotBlank
                                 @Length(max = 150)
                                 String name,

                                 @NotBlank
                                 @Length(max = 100)
                                 String location,

                                 @Future
                                 LocalDate date,

                                 @NotNull
                                 Set<Long> categoriesIds
) implements Serializable {
}