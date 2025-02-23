package ro.unibuc.fmi.karate_management_platform.models.competition.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.kumite.KumiteDivisionType;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum AgeGroup {
    CHILDREN_U7("Children U7", KumiteDivisionType.HEIGHT),
    CHILDREN_7_9("Children 7-9", KumiteDivisionType.HEIGHT),
    CHILDREN_10_12("Children 10-12", KumiteDivisionType.HEIGHT),
    CADETS_13_14("Cadets 13-14", KumiteDivisionType.WEIGHT),
    JUNIORS_15_17("Juniors 15-17", KumiteDivisionType.WEIGHT),
    SENIORS_18_34("Seniors 18-34", KumiteDivisionType.WEIGHT),
    VETERANS_35_44("Veterans 35-44", KumiteDivisionType.WEIGHT),
    VETERANS_45_64("Veterans 45-64", KumiteDivisionType.WEIGHT),
    VETERANS_65_PLUS("Veterans 65+", KumiteDivisionType.WEIGHT);

    private final String description;
    private final KumiteDivisionType kumiteDivisionType;

    public static final Set<AgeGroup> ALL_AGE_GROUPS = Set.of(
            CHILDREN_U7, CHILDREN_7_9, CHILDREN_10_12,
            CADETS_13_14, JUNIORS_15_17, SENIORS_18_34,
            VETERANS_35_44, VETERANS_45_64, VETERANS_65_PLUS);

    public static final Set<AgeGroup> ADULT_AGE_GROUPS = Set.of(CADETS_13_14, JUNIORS_15_17, SENIORS_18_34, VETERANS_35_44, VETERANS_45_64, VETERANS_65_PLUS);
}