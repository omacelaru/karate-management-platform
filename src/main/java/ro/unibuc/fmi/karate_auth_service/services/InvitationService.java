package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.models.invitation.Invitation;
import ro.unibuc.fmi.karate_auth_service.models.invitation.InvitationStatus;
import ro.unibuc.fmi.karate_auth_service.repositories.InvitationRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    @Value("${domain.url}")
    private String domainUrl;

    public void sendInvitation(String email, Long createdBy) {
        log.info("Sending invitation to email: {}", email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        log.debug("Generated token: {} with expiration date: {}", token, expiresAt);

        Invitation invitation = Invitation.builder()
                .email(email)
                .token(token)
                .status(InvitationStatus.PENDING)
                .expiresAt(expiresAt)
                .createdBy(createdBy)
                .build();

        invitationRepository.save(invitation);

        String link = domainUrl + "/invitations/accept?token=" + token;
        String text = "Click here to accept the invitation: " + link;
        log.debug("Generated invitation link: {}", link);

        emailService.sendInvitationEmail(email, "Invitation", text);

    }

    public void acceptInvitation(String token) {
        log.info("Accepting invitation with token: {}", token);
        Optional<Invitation> optionalInvitation = invitationRepository.findByToken(token);

        if (optionalInvitation.isEmpty()) {
            log.error("Invitation with token {} not found.", token);
            throw new IllegalArgumentException("Invitation not found.");
        }

        Invitation invitation = optionalInvitation.get();

        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.error("Invitation with token {} expired.", token);
            invitation.setStatus(InvitationStatus.EXPIRED);
            throw new IllegalArgumentException("Invitation expired.");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }
}
