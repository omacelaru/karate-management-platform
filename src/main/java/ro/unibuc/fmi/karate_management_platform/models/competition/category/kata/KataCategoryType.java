package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KataCategoryType {
    WHITE_TO_ORANGE("White to Orange"),
    GREEN_TO_BLUE("Green to Blue"),
    BROWN_TO_BLACK("Brown to Black"),
    OPEN("Open");

    private final String value;
}
