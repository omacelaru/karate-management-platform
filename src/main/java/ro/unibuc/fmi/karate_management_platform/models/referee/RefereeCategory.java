package ro.unibuc.fmi.karate_management_platform.models.referee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RefereeCategory {
    A("A"),
    B("B"),
    C("C"),
    D("D");

    private final String category;
}
