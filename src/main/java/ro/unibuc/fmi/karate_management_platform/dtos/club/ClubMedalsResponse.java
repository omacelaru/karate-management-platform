package ro.unibuc.fmi.karate_management_platform.dtos.club;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Response containing club details and medal counts.")
public record ClubMedalsResponse(
        ClubResponse club,
        int gold,
        int silver,
        int bronze
) implements Serializable {

}