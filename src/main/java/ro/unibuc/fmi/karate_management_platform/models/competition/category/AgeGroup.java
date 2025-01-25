package ro.unibuc.fmi.karate_management_platform.models.competition.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeGroup {
    CHILDREN_U7("Children U7"),
    CHILDREN_7_9("Children 7-9"),
    CHILDREN_10_12("Children 10-12"),
    CADETS_13_14("Cadets 13-14"),
    JUNIORS_15_17("Juniors 15-17"),
    SENIORS_18_34("Seniors 18-34"),
    VETERANS_35_44("Veterans 35-44"),
    VETERANS_45_64("Veterans 45-64"),
    VETERANS_65_PLUS("Veterans 65+");

    private final String value;

}