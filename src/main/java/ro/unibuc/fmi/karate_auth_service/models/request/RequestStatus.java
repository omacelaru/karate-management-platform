package ro.unibuc.fmi.karate_auth_service.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    IN_PROGRESS("IN_PROGRESS"),
    PARTIALLY_COMPLETED("PARTIALLY_COMPLETED"),
    REJECTED("REJECTED"),
    REVOKED("REVOKED");

    private final String status;
}
