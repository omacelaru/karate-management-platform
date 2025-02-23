package ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KumiteTeamType {
    ROTATION("Rotation"),
    SIMPLE("Simple");

    private final String value;
}
