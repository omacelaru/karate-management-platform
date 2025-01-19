package ro.unibuc.fmi.karate_management_platform.dtos.club;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for club request data, used to create or update a club's information.
 * This class includes details such as name and acronym.
 * <p>Usage: This DTO is used for passing club data from the client to the backend when creating or updating a club's profile.</p>
 */
@Schema(description = "Club request containing details such as name and acronym.")
public record ClubRequest(@NotBlank(message = "Name is mandatory")
                          @Length(max = 60, message = "Name must have at most 60 characters")
                          String name,

                          @Pattern(regexp = "^[A-Z]{3,10}$",
                                  message = "Acronym must have between 3 and 10 uppercase letters")
                          String acronym
) implements Serializable {
}