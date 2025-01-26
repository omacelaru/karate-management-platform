package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.Category;

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

                                 @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
                                 Set<Category> categories
) implements Serializable {
}