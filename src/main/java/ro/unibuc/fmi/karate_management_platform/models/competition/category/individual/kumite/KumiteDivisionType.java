package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KumiteDivisionType {
    WEIGHT("Weight"),
    HEIGHT("Height");

    private final String value;
}
