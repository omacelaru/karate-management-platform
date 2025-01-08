package ro.unibuc.fmi.karate_auth_service.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestType {
    COACH_CREATION("COACH_CREATION"),
    REFEREE_CREATION("REFEREE_CREATION");

    private final String type;
}
