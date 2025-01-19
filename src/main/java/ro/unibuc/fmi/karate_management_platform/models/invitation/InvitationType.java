package ro.unibuc.fmi.karate_management_platform.models.invitation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationType {
    USER_INVITATION("USER_INVITATION");

    private final String type;
}
