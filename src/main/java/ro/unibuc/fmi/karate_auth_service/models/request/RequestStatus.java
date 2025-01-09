package ro.unibuc.fmi.karate_auth_service.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

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

    public static final Set<RequestStatus> ACTIVE_STATUSES = Set.of(PENDING, IN_PROGRESS, PARTIALLY_COMPLETED);
}
