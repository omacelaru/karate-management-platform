package ro.unibuc.fmi.karate_auth_service.strategies.invitationType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_auth_service.models.invitation.Invitation;
import ro.unibuc.fmi.karate_auth_service.models.invitation.InvitationType;
import ro.unibuc.fmi.karate_auth_service.services.EmailService;
import ro.unibuc.fmi.karate_auth_service.services.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInvitationTypeStrategy implements InvitationTypeStrategy {
    @Getter
    private final InvitationType invitationType = InvitationType.USER_INVITATION;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public void handleAcceptInvitation(Invitation invitation) {
        log.info("Handling accept invitation for user: {}", invitation.getEmail());

        String email = invitation.getEmail();
        String password = UserService.generatePassword();

        emailService.sendCredentialsEmail(email, password);

        userService.createUser(email, password);
    }
}
