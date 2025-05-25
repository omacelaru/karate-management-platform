package ro.unibuc.fmi.karate_management_platform.models.competition.category.individual.kumite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Athlete;
import ro.unibuc.fmi.karate_management_platform.models.athelte.Gender;
import ro.unibuc.fmi.karate_management_platform.models.competition.category.AgeGroup;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ro.unibuc.fmi.karate_management_platform.models.athelte.Gender.*;

@Getter
@AllArgsConstructor
public enum KumiteDivisionRange {
    UNDER_110CM(Map.of(AgeGroup.CHILDREN_U7, ALL_GENDERS()), "Under 110cm", KumiteDivisionType.HEIGHT, height -> height < 110),

    OVER_110CM(Map.of(AgeGroup.CHILDREN_U7, ALL_GENDERS()), "Over 110cm", KumiteDivisionType.HEIGHT, height -> height >= 110),

    UNDER_130CM(Map.of(AgeGroup.CHILDREN_7_9, ALL_GENDERS()), "Under 130cm", KumiteDivisionType.HEIGHT, height -> height < 130),

    BETWEEN_130_140CM(Map.of(AgeGroup.CHILDREN_7_9, ALL_GENDERS()), "Between 130-140cm", KumiteDivisionType.HEIGHT, height -> height >= 130 && height < 140),

    OVER_140CM(Map.of(AgeGroup.CHILDREN_7_9, ALL_GENDERS()), "Over 140cm", KumiteDivisionType.HEIGHT, height -> height >= 140),

    UNDER_140CM(Map.of(AgeGroup.CHILDREN_10_12, ALL_GENDERS()), "Under 140cm", KumiteDivisionType.HEIGHT, height -> height < 140),

    BETWEEN_140_150CM(Map.of(AgeGroup.CHILDREN_10_12, ALL_GENDERS()), "Between 140-150cm", KumiteDivisionType.HEIGHT, height -> height >= 140 && height < 150),

    OVER_150CM(Map.of(AgeGroup.CHILDREN_10_12, ALL_GENDERS()), "Over 150cm", KumiteDivisionType.HEIGHT, height -> height >= 150),


    UNDER_50KG(Map.of(AgeGroup.CADETS_13_14, ALL_GENDERS(), AgeGroup.JUNIORS_15_17, FEMALE()), "Under 50kg", KumiteDivisionType.WEIGHT, weight -> weight < 50),

    UNDER_55KG(Map.of(AgeGroup.JUNIORS_15_17, MALE(), AgeGroup.SENIORS_18_34, FEMALE()), "Under 55kg", KumiteDivisionType.WEIGHT, weight -> weight < 55),

    UNDER_65KG(Map.of(AgeGroup.SENIORS_18_34, MALE(), AgeGroup.VETERANS_35_44, ALL_GENDERS(), AgeGroup.VETERANS_45_64, ALL_GENDERS(), AgeGroup.VETERANS_65_PLUS, ALL_GENDERS()),
            "Under 65kg", KumiteDivisionType.WEIGHT, weight -> weight < 65),


    BETWEEN_50_55KG(Map.of(AgeGroup.CADETS_13_14, ALL_GENDERS(), AgeGroup.JUNIORS_15_17, FEMALE()), "Between 50-55kg", KumiteDivisionType.WEIGHT, weight -> weight >= 50 && weight < 55),

    BETWEEN_55_60KG(Map.of(AgeGroup.JUNIORS_15_17, ALL_GENDERS(), AgeGroup.SENIORS_18_34, FEMALE(), AgeGroup.CADETS_13_14, ALL_GENDERS()), "Between 55-60kg", KumiteDivisionType.WEIGHT, weight -> weight >= 55 && weight < 60),

    BETWEEN_60_65KG(Map.of(AgeGroup.JUNIORS_15_17, MALE(), AgeGroup.SENIORS_18_34, FEMALE()), "Between 60-65kg", KumiteDivisionType.WEIGHT, weight -> weight >= 60 && weight < 65),

    BETWEEN_65_70KG(Map.of(AgeGroup.SENIORS_18_34, MALE(), AgeGroup.VETERANS_35_44, ALL_GENDERS(), AgeGroup.VETERANS_45_64, ALL_GENDERS(), AgeGroup.VETERANS_65_PLUS, ALL_GENDERS()),
            "Between 65-70kg", KumiteDivisionType.WEIGHT, weight -> weight >= 65 && weight < 70),

    BETWEEN_70_75KG(Map.of(AgeGroup.SENIORS_18_34, MALE(), AgeGroup.VETERANS_35_44, ALL_GENDERS(), AgeGroup.VETERANS_45_64, ALL_GENDERS(), AgeGroup.VETERANS_65_PLUS, ALL_GENDERS()),
            "Between 70-75kg", KumiteDivisionType.WEIGHT, weight -> weight >= 70 && weight < 75),


    OVER_60KG(Map.of(AgeGroup.CADETS_13_14, ALL_GENDERS(), AgeGroup.JUNIORS_15_17, FEMALE()), "Over 60kg", KumiteDivisionType.WEIGHT, weight -> weight >= 60),

    OVER_65KG(Map.of(AgeGroup.JUNIORS_15_17, MALE(), AgeGroup.SENIORS_18_34, FEMALE()), "Over 65kg", KumiteDivisionType.WEIGHT, weight -> weight >= 65),
    OVER_75KG(Map.of(AgeGroup.SENIORS_18_34, MALE(), AgeGroup.VETERANS_35_44, ALL_GENDERS(), AgeGroup.VETERANS_45_64, ALL_GENDERS(), AgeGroup.VETERANS_65_PLUS, ALL_GENDERS()),
            "Over 75kg", KumiteDivisionType.WEIGHT, weight -> weight >= 75),

    OPEN(Map.of(AgeGroup.CHILDREN_U7, ALL_GENDERS(), AgeGroup.CHILDREN_7_9, ALL_GENDERS(), AgeGroup.CHILDREN_10_12, ALL_GENDERS(), AgeGroup.CADETS_13_14, ALL_GENDERS(), AgeGroup.JUNIORS_15_17, ALL_GENDERS(), AgeGroup.SENIORS_18_34, ALL_GENDERS(), AgeGroup.VETERANS_35_44, ALL_GENDERS(), AgeGroup.VETERANS_45_64, ALL_GENDERS(), AgeGroup.VETERANS_65_PLUS, ALL_GENDERS()), "Open", KumiteDivisionType.HEIGHT, height -> true);

    private final Map<AgeGroup, Set<Gender>> genderAgeGroupMap;
    private final String value;
    private final KumiteDivisionType type;
    private final Function<Short, Boolean> rangeCheck;

    public static Set<KumiteDivisionRange> getAvailableDivisions(AgeGroup ageGroup, Gender gender) {
        return Arrays.stream(KumiteDivisionRange.values())
                .filter(division -> division.genderAgeGroupMap.containsKey(ageGroup))
                .filter(division -> division.genderAgeGroupMap.get(ageGroup).contains(gender))
                .collect(Collectors.toSet());
    }

    public static KumiteDivisionRange getAvailableDivisions(AgeGroup ageGroup, Gender gender, short value) {
        return Arrays.stream(KumiteDivisionRange.values())
                .filter(division -> division.genderAgeGroupMap.containsKey(ageGroup))
                .filter(division -> division.genderAgeGroupMap.get(ageGroup).contains(gender))
                .filter(division -> division.rangeCheck.apply(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching division found for the given criteria."));
    }

    public static KumiteDivisionRange getKumiteDivisionRange(Athlete athlete, AgeGroup ageGroup) {
        if (KumiteDivisionType.HEIGHT.equals(ageGroup.getKumiteDivisionType())) {
            return getAvailableDivisions(ageGroup, athlete.getUser().getGender(), athlete.getHeight());
        } else {
            return getAvailableDivisions(ageGroup, athlete.getUser().getGender(), athlete.getWeight());
        }
    }
    }
