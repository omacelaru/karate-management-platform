package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IndividualCategoryType {
    KATA_INDIVIDUAL("Kata Individual"),
    KUMITE_INDIVIDUAL("Kumite Individual");

    private final String value;
}
