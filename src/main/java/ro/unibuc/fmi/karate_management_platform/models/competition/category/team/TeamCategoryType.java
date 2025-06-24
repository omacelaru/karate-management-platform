package ro.unibuc.fmi.karate_management_platform.models.competition.category.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamCategoryType {
    KATA_TEAM("Kata Team"),
    KUMITE_TEAM("Kumite Team");

    private final String value;
}
