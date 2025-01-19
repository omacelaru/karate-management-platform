package ro.unibuc.fmi.karate_management_platform.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${domain.basePath}")
    private String basePath;


    private void sendEmail(String to, String subject, String text) {
        log.info("Sending email to: {}", to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendCredentialsEmail(String email, String password){
        log.info("Sending credentials email to: {}", email);
        String subject = "Welcome to Karate Club";
        String resetPasswordUrl = "http://localhost:8080/reset-password";
        String text = """
            <html>
            <body>
                <h1>Welcome to Karate Club!</h1>
                <p>Dear User,</p>
                <p>We are excited to have you join us. Here are your credentials to access our system:</p>
                <ul>
                    <li><strong>Email:</strong> %s</li>
                    <li><strong>Password:</strong> %s</li>
                </ul>
                <p>For your security, please change your password after your first login. You can do so by clicking the link below:</p>
                <p><a href="%s">Change your password</a></p>
                <p>Best regards,<br>Karate Club Team</p>
            </body>
            </html>
            """.formatted(email, password, resetPasswordUrl);
        sendEmail(email, subject, text);
    }

    public void sendInvitationEmail(String email, String token) {
        log.info("Sending invitation email to: {}", email);
        String subject = "Invitation to Karate Club";
        String link = domainUrl + basePath + "/invitations/accept?token=" + token;
        String text = """
            <html>
            <body>
                <h1>Invitation to Karate Club</h1>
                <p>Dear User,</p>
                <p>You have been invited to join Karate Club. Click the link below to accept the invitation:</p>
                <p><a href="%s">Accept invitation</a></p>
                <p>Best regards,<br>Karate Club Team</p>
            </body>
            </html>
            """.formatted(link);
        sendEmail(email, subject, text);
    }
}
