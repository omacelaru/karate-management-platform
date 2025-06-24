package ro.unibuc.fmi.karate_management_platform.dtos.competition.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.karate_management_platform.dtos.athlete.AthleteResponse;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.Team;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Team}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse implements Serializable {
    private Long id;
    private String teamName;
    private Set<AthleteResponse> athletes;
}