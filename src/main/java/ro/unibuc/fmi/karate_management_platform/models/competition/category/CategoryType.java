package ro.unibuc.fmi.karate_management_platform.models.competition.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    KATA_INDIVIDUAL("Kata Individual"),
    KATA_TEAM("Kata Team"),
    KUMITE_INDIVIDUAL("Kumite Individual"),
    KUMITE_TEAM("Kumite Team");

    private final String value;
}
