package ro.unibuc.fmi.karate_management_platform.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailServiceTest {

    private EmailService emailService;
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        MessageSource messageSource = mock(MessageSource.class);
        emailService = new EmailService(javaMailSender, messageSource);
    }

    @Test
    void sendCredentialsEmail_shouldSendEmailWithCorrectDetails() {
        String email = "user@example.com";
        String password = "password123";
        String expectedSubject = "Welcome to Karate Club";
        String expectedText = """
                <html>
                <body>
                    <h1>Welcome to Karate Club!</h1>
                    <p>Dear User,</p>
                    <p>We are excited to have you join us. Here are your credentials to access our system:</p>
                    <ul>
                        <li><strong>Email:</strong> user@example.com</li>
                        <li><strong>Password:</strong> password123</li>
                    </ul>
                    <p>For your security, please change your password after your first login. You can do so by clicking the link below:</p>
                    <p><a href="http://localhost:8080/reset-password">Change your password</a></p>
                    <p>Best regards,<br>Karate Club Team</p>
                </body>
                </html>
                """;

        emailService.sendCredentialsEmail(email, password, "en");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.getTo()).containsExactly(email);
        assertThat(capturedMessage.getSubject()).isEqualTo(expectedSubject);
        assertThat(capturedMessage.getText()).isEqualTo(expectedText);
    }

    @Test
    void sendInvitationEmail_shouldSendEmailWithCorrectDetails() {
        String email = "user@example.com";
        String token = "invitation-token";
        String domainUrl = "http://localhost:8080";
        String basePath = "/api";
        String expectedSubject = "Invitation to Karate Club";
        String expectedText = """
                <html>
                <body>
                    <h1>Invitation to Karate Club</h1>
                    <p>Dear User,</p>
                    <p>You have been invited to join Karate Club. Click the link below to accept the invitation:</p>
                    <p><a href="http://localhost:8080/api/invitations/accept?token=invitation-token">Accept invitation</a></p>
                    <p>Best regards,<br>Karate Club Team</p>
                </body>
                </html>
                """;

        emailService.sendInvitationEmail(email, token, "en");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());
        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        assertThat(capturedMessage.getTo()).containsExactly(email);
        assertThat(capturedMessage.getSubject()).isEqualTo(expectedSubject);
        //assertThat(capturedMessage.getText()).isEqualTo(expectedText);
    }
}