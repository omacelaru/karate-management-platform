package ro.unibuc.fmi.karate_management_platform.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestType {
    COACH_CREATION("COACH_CREATION"),
    REFEREE_CREATION("REFEREE_CREATION"),
    ATHLETE_CREATION("ATHLETE_CREATION"),
    ORGANIZER_CREATION("ORGANIZER_CREATION"),
    COMPETITION_CREATION("COMPETITION_CREATION");

    private final String type;

    public RequestScope getRequestScope() {
        return switch (this) {
            case COACH_CREATION, REFEREE_CREATION, ORGANIZER_CREATION, COMPETITION_CREATION -> RequestScope.ROLES;
            case ATHLETE_CREATION -> RequestScope.USERS;
        };
    }
}
