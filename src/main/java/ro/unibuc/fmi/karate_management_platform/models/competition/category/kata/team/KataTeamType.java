package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KataTeamType {
    SIMPLE("Simple");

    private final String value;
}
