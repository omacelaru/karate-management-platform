package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    private void sendEmail(String to, String subject, String htmlContent) {
        log.info("Sending email to: {}", to);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indicates HTML content
            
            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendCredentialsEmail(String email, String password) {
        log.info("Sending credentials email to: {}", email);
        String subject = "Welcome to Karate Club";
        String resetPasswordUrl = domainUrl + "/reset-password";
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f8f9fa; padding: 20px; text-align: center; }
                    .content { padding: 20px; }
                    .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    .footer { text-align: center; padding: 20px; font-size: 0.9em; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to Karate Club!</h1>
                    </div>
                    <div class="content">
                        <p>Dear User,</p>
                        <p>We are excited to have you join us. Here are your credentials to access our system:</p>
                        <ul>
                            <li><strong>Email:</strong> %s</li>
                            <li><strong>Password:</strong> %s</li>
                        </ul>
                        <p>For your security, please change your password after your first login. You can do so by clicking the button below:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Change your password</a>
                        </p>
                    </div>
                    <div class="footer">
                        <p>Best regards,<br>Karate Club Team</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(email, password, resetPasswordUrl);
        sendEmail(email, subject, htmlContent);
    }

    public void sendInvitationEmail(String email, String token) {
        log.info("Sending invitation email to: {}", email);
        String subject = "Invitation to Karate Club";
        String link = domainUrl + basePath + "/invitations/accept?token=" + token;
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f8f9fa; padding: 20px; text-align: center; }
                    .content { padding: 20px; }
                    .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    .footer { text-align: center; padding: 20px; font-size: 0.9em; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Invitation to Karate Club</h1>
                    </div>
                    <div class="content">
                        <p>Dear User,</p>
                        <p>You have been invited to join Karate Club. Click the button below to accept the invitation:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Accept invitation</a>
                        </p>
                    </div>
                    <div class="footer">
                        <p>Best regards,<br>Karate Club Team</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(link);
        sendEmail(email, subject, htmlContent);
    }

    public void sendConfirmationEmail(String email, String token) {
        log.info("Sending confirmation email to: {}", email);
        String subject = "Confirm Your Email - Karate Club";
        String confirmationLink = domainUrl + "/auth/confirm-email?token=" + token;
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background-color: #f8f9fa; padding: 20px; text-align: center; }
                    .content { padding: 20px; }
                    .button { display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
                    .footer { text-align: center; padding: 20px; font-size: 0.9em; color: #666; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Welcome to Karate Club!</h1>
                    </div>
                    <div class="content">
                        <p>Dear User,</p>
                        <p>Thank you for registering. Please confirm your email by clicking the button below:</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">Confirm Email</a>
                        </p>
                        <p>If you did not create an account, please ignore this email.</p>
                    </div>
                    <div class="footer">
                        <p>Best regards,<br>Karate Club Team</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(confirmationLink);
        sendEmail(email, subject, htmlContent);
    }
}
