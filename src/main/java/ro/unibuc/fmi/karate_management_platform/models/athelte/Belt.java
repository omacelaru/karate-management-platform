package ro.unibuc.fmi.karate_management_platform.models.athelte;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Belt {
    WHITE("White"),
    YELLOW("Yellow"),
    ORANGE("Orange"),
    GREEN("Green"),
    BLUE("Blue"),
    BROWN("Brown"),
    BLACK("Black");

    private final String value;
}
