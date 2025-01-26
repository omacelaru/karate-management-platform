package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ro.unibuc.fmi.karate_management_platform.dtos.competition.category.CategoryIDsRequest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link ro.unibuc.fmi.karate_management_platform.models.competition.Competition}
 */
public record CompetitionRequest(@NotBlank
                                 @Length(max = 150)
                                 String name,

                                 @NotBlank
                                 @Length(max = 100)
                                 String location,

                                 @Future
                                 LocalDate date,

                                 @NotNull
                                 List<CategoryIDsRequest> categories
) implements Serializable {
}