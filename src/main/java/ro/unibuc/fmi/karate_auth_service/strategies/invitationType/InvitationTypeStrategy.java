package ro.unibuc.fmi.karate_auth_service.strategies.invitationType;

import ro.unibuc.fmi.karate_auth_service.models.invitation.Invitation;
import ro.unibuc.fmi.karate_auth_service.models.invitation.InvitationType;

public interface InvitationTypeStrategy {
    InvitationType getInvitationType();

    void handleAcceptInvitation(Invitation invitation);
}
