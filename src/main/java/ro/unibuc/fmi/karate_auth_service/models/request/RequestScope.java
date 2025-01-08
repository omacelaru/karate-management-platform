package ro.unibuc.fmi.karate_auth_service.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestScope {
    USERS("USERS"),
    ROLES("ROLES");

    private final String scope;
}
