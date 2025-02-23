package ro.unibuc.fmi.karate_management_platform.models.competition.category.team.kumite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KumiteTeamType {
    ROTATION("Rotation"),
    SIMPLE("Simple");

    private final String value;
}
