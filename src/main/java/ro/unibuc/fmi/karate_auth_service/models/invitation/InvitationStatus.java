package ro.unibuc.fmi.karate_auth_service.models.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationStatus {
    PENDING("PENDING"),
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    EXPIRED("EXPIRED");

    private final String status;
}
