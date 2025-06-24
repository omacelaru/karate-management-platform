package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ro.unibuc.fmi.karate_management_platform.factories.InvitationTypeStrategyFactory;
import ro.unibuc.fmi.karate_management_platform.models.invitation.Invitation;
import ro.unibuc.fmi.karate_management_platform.models.invitation.InvitationStatus;
import ro.unibuc.fmi.karate_management_platform.models.invitation.InvitationType;
import ro.unibuc.fmi.karate_management_platform.repositories.InvitationRepository;
import ro.unibuc.fmi.karate_management_platform.strategies.invitationType.InvitationTypeStrategy;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class InvitationServiceTest {

    private InvitationService invitationService;
    private InvitationRepository invitationRepository;
    private InvitationTypeStrategyFactory invitationTypeStrategyFactory;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        invitationRepository = mock(InvitationRepository.class);
        invitationTypeStrategyFactory = mock(InvitationTypeStrategyFactory.class);
        emailService = mock(EmailService.class);
        invitationService = new InvitationService(invitationRepository, invitationTypeStrategyFactory, emailService);
    }

    @Test
    void sendInvitation_shouldSaveInvitationAndSendEmail() {
        String email = "user@example.com";
        Long createdBy = 1L;

        invitationService.sendInvitation(email, createdBy);

        ArgumentCaptor<Invitation> invitationCaptor = ArgumentCaptor.forClass(Invitation.class);
        verify(invitationRepository).save(invitationCaptor.capture());
        Invitation savedInvitation = invitationCaptor.getValue();

        assertThat(savedInvitation.getEmail()).isEqualTo(email);
        assertThat(savedInvitation.getCreatedBy()).isEqualTo(createdBy);
        assertThat(savedInvitation.getStatus()).isEqualTo(InvitationStatus.PENDING);
        assertThat(savedInvitation.getType()).isEqualTo(InvitationType.USER_INVITATION);
        verify(emailService).sendInvitationEmail(eq(email), eq(savedInvitation.getToken()), eq("en"));
    }

    @Test
    void acceptInvitation_shouldAcceptValidInvitation() {
        String token = UUID.randomUUID().toString();
        Invitation invitation = Invitation.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().plusDays(1))
                .status(InvitationStatus.PENDING)
                .type(InvitationType.USER_INVITATION)
                .build();

        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(invitationTypeStrategyFactory.getStrategy(invitation.getType())).thenReturn(mock(InvitationTypeStrategy.class));

        invitationService.acceptInvitation(token);

        verify(invitationRepository).save(invitation);
        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
        verify(invitationTypeStrategyFactory).getStrategy(invitation.getType());
    }

    @Test
    void acceptInvitation_shouldThrowExceptionIfTokenNotFound() {
        String token = UUID.randomUUID().toString();

        when(invitationRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invitationService.acceptInvitation(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invitation not found.");

        verify(invitationRepository).findByToken(token);
        verifyNoInteractions(invitationTypeStrategyFactory);
    }

    @Test
    void acceptInvitation_shouldThrowExceptionIfInvitationExpired() {
        String token = UUID.randomUUID().toString();
        Invitation invitation = Invitation.builder()
                .token(token)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .status(InvitationStatus.PENDING)
                .build();

        when(invitationRepository.findByToken(token)).thenReturn(Optional.of(invitation));
        when(invitationTypeStrategyFactory.getStrategy(invitation.getType())).thenReturn(mock(InvitationTypeStrategy.class));

        assertThatThrownBy(() -> invitationService.acceptInvitation(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invitation expired.");

        assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.EXPIRED);
    }
}