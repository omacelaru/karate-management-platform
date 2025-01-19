package ro.unibuc.fmi.karate_management_platform.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.unibuc.fmi.karate_management_platform.models.invitation.Invitation;
import ro.unibuc.fmi.karate_management_platform.models.invitation.InvitationStatus;
import ro.unibuc.fmi.karate_management_platform.repositories.InvitationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InvitationScheduler {
    private final InvitationRepository invitationRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void expirePendingInvitations() {
        log.info("Expiring pending invitations...");
        List<Invitation> pendingInvitations = invitationRepository.findAll().stream()
                .filter(invitation -> invitation.getExpiresAt().isBefore(LocalDateTime.now())
                        && invitation.getStatus() == InvitationStatus.PENDING)
                .toList();

        pendingInvitations.forEach(invitation -> invitation.setStatus(InvitationStatus.EXPIRED));
        invitationRepository.saveAll(pendingInvitations);

        log.info("Expired {} pending invitations.", pendingInvitations.size());
    }
}
