package ro.unibuc.fmi.karate_management_platform.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestScope {
    USERS("USERS"),
    ROLES("ROLES");

    private final String scope;
}
