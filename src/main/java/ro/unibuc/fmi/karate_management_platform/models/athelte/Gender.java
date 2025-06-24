package ro.unibuc.fmi.karate_management_platform.models.athelte;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String value;

    public static Set<Gender> ALL_GENDERS() {
        return Set.of(Gender.values());
    }

    public static Set<Gender> MALE() {
        return Set.of(Gender.MALE, Gender.OTHER);
    }

    public static Set<Gender> FEMALE() {
        return Set.of(Gender.FEMALE, Gender.OTHER);
    }
}
