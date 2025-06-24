package ro.unibuc.fmi.karate_management_platform.dtos.competition;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

/**
 * DTO for {@link ro.unibuc.fmi.karate_management_platform.models.competition.Competition}
 */
@Embeddable
public record CompetitionRequest(@NotBlank(message = "Name is mandatory")
                                 @Length(max = 255, message = "Name must have at most 150 characters")
                                 @Column(nullable = false)
                                 @JdbcTypeCode(SqlTypes.VARCHAR)
                                 String name,

                                 @NotBlank(message = "Location is mandatory")
                                 @Length(max = 255, message = "Location must have at most 100 characters")
                                 @Column(nullable = false)
                                 @JdbcTypeCode(SqlTypes.VARCHAR)
                                 String location,

                                 @Future(message = "Date must be in the future")
                                 @Column(nullable = false)
                                 LocalDate date
) implements Serializable {

}