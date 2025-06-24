package ro.unibuc.fmi.karate_management_platform.strategies.invitationType;

import ro.unibuc.fmi.karate_management_platform.models.invitation.Invitation;
import ro.unibuc.fmi.karate_management_platform.models.invitation.InvitationType;

public interface InvitationTypeStrategy {
    InvitationType getInvitationType();

    void handleAcceptInvitation(Invitation invitation);
}
