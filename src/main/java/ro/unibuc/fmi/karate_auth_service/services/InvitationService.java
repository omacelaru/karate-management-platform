package ro.unibuc.fmi.karate_auth_service.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.karate_auth_service.factories.InvitationTypeStrategyFactory;
import ro.unibuc.fmi.karate_auth_service.models.invitation.Invitation;
import ro.unibuc.fmi.karate_auth_service.models.invitation.InvitationStatus;
import ro.unibuc.fmi.karate_auth_service.models.invitation.InvitationType;
import ro.unibuc.fmi.karate_auth_service.repositories.InvitationRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final InvitationTypeStrategyFactory invitationTypeStrategyFactory;
    private final EmailService emailService;

    public void sendInvitation(String email, Long createdBy) {
        log.info("Sending invitation to email: {}", email);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(7);
        log.debug("Generated token: {} with expiration date: {}", token, expiresAt);

        Invitation invitation = Invitation.builder()
                .email(email)
                .token(token)
                .status(InvitationStatus.PENDING)
                .type(InvitationType.USER_INVITATION)
                .expiresAt(expiresAt)
                .createdBy(createdBy)
                .build();

        invitationRepository.save(invitation);

        emailService.sendInvitationEmail(email, token);
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
        invitationTypeStrategyFactory.getStrategy(invitation.getType()).handleAcceptInvitation(invitation);
        invitationRepository.save(invitation);
    }
}
