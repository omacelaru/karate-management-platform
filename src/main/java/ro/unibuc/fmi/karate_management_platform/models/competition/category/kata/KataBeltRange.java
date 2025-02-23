package ro.unibuc.fmi.karate_management_platform.models.competition.category.kata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Belt;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup.ADULT_AGE_GROUPS;
import static ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup.ALL_AGE_GROUPS;

@Getter
@AllArgsConstructor
public enum KataBeltRange {
    WHITE_TO_ORANGE("White to Orange", ALL_AGE_GROUPS),
    GREEN_TO_BLUE("Green to Blue", ALL_AGE_GROUPS),
    BROWN_TO_BLACK("Brown to Black", ADULT_AGE_GROUPS),
    OPEN("Open", ALL_AGE_GROUPS);

    private final String value;
    private final Set<AgeGroup> ageGroups;

    public static KataBeltRange getKataBeltRange(Belt belt) {
        if (belt == Belt.WHITE || belt == Belt.YELLOW || belt == Belt.ORANGE) {
            return WHITE_TO_ORANGE;
        } else if (belt == Belt.GREEN || belt == Belt.BLUE) {
            return GREEN_TO_BLUE;
        } else if (belt == Belt.BROWN || belt == Belt.BLACK) {
            return BROWN_TO_BLACK;
        } else {
            //todo case for OPEN
            return OPEN;
        }
    }

    public static KataBeltRange getKataBeltRange(String belt) {
        if (belt.equals("WHITE") || belt.equals("YELLOW") || belt.equals("ORANGE")) {
            return WHITE_TO_ORANGE;
        } else if (belt.equals("GREEN") || belt.equals("BLUE")) {
            return GREEN_TO_BLUE;
        } else if (belt.equals("BROWN") || belt.equals("BLACK")) {
            return BROWN_TO_BLACK;
        } else {
            //todo case for OPEN
            return OPEN;
        }
    }

    public static Set<KataBeltRange> getAvailableKataBeltRanges(AgeGroup ageGroup) {
        return Arrays.stream(KataBeltRange.values())
                .filter(kataBeltRange -> kataBeltRange.getAgeGroups().contains(ageGroup))
                .collect(Collectors.toSet());
    }
}
