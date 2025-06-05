package ro.unibuc.fmi.karate_management_platform.models.competition.match;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchType {
    KATA_INDIVIDUAL("Kata Individual"),
    KUMITE_INDIVIDUAL("Kumite Individual"),
    KATA_TEAM("Kata Team"),
    KUMITE_TEAM("Kumite Team");

    private final String value;
}
