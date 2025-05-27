package ro.unibuc.fmi.karate_management_platform.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${domain.basePath}")
    private String basePath;

    private void sendEmail(String to, String subject, String htmlContent) {
        log.info("Sending email to: {} with subject: {}", to, subject);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void setLocaleForEmail(String language) {
        try {
            Locale locale = new Locale(language);
            LocaleContextHolder.setLocale(locale, true);
            log.info("Set locale to: {} for email", locale);
        } catch (Exception e) {
            log.warn("Invalid language code: {}, falling back to default locale", language);
            LocaleContextHolder.setLocale(Locale.getDefault(), true);
        }
    }

    private String getMessage(String code, Object... args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        log.info("Getting message for code: {} with locale: {}", code, currentLocale);
        String message = messageSource.getMessage(code, args, currentLocale);
        log.info("Retrieved message: {}", message);
        return message;
    }

    public void sendCredentialsEmail(String email, String password, String language) {
        log.info("Sending credentials email to: {} in language: {}", email, language);
        setLocaleForEmail(language);
        
        String subject = getMessage("emails.welcome.subject");
        String resetPasswordUrl = String.format("%s%s/%s/auth/reset-password", domainUrl, basePath, language);
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
                        <h1>%s</h1>
                    </div>
                    <div class="content">
                        <p>%s</p>
                        <p>%s</p>
                        <ul>
                            <li><strong>%s:</strong> %s</li>
                            <li><strong>%s:</strong> %s</li>
                        </ul>
                        <p>%s</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">%s</a>
                        </p>
                    </div>
                    <div class="footer">
                        <p>%s<br>%s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                getMessage("emails.welcome.title"),
                getMessage("emails.welcome.greeting"),
                getMessage("emails.welcome.credentials"),
                getMessage("emails.welcome.email"),
                email,
                getMessage("emails.welcome.password"),
                password,
                getMessage("emails.welcome.security"),
                resetPasswordUrl,
                getMessage("emails.welcome.changePassword"),
                getMessage("emails.welcome.regards"),
                getMessage("emails.welcome.team")
            );
        sendEmail(email, subject, htmlContent);
    }

    public void sendInvitationEmail(String email, String token, String language) {
        log.info("Sending invitation email to: {} in language: {}", email, language);
        setLocaleForEmail(language);
        
        String subject = getMessage("emails.invitation.subject");
        String link = String.format("%s%s/%s/auth/invitations/accept?token=%s", domainUrl, basePath, language, token);
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
                        <h1>%s</h1>
                    </div>
                    <div class="content">
                        <p>%s</p>
                        <p>%s</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">%s</a>
                        </p>
                    </div>
                    <div class="footer">
                        <p>%s<br>%s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                getMessage("emails.invitation.title"),
                getMessage("emails.invitation.greeting"),
                getMessage("emails.invitation.message"),
                link,
                getMessage("emails.invitation.acceptInvitation"),
                getMessage("emails.invitation.regards"),
                getMessage("emails.invitation.team")
            );
        sendEmail(email, subject, htmlContent);
    }

    public void sendConfirmationEmail(String email, String token, String language) {
        log.info("Sending confirmation email to: {} in language: {}", email, language);
        setLocaleForEmail(language);
        
        String subject = getMessage("emails.confirmation.subject");
        String confirmationLink = String.format("%s%s/%s/auth/verify-email?token=%s", domainUrl, basePath, language, token);
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
                        <h1>%s</h1>
                    </div>
                    <div class="content">
                        <p>%s</p>
                        <p>%s</p>
                        <p style="text-align: center;">
                            <a href="%s" class="button">%s</a>
                        </p>
                        <p>%s</p>
                    </div>
                    <div class="footer">
                        <p>%s<br>%s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(
                getMessage("emails.confirmation.title"),
                getMessage("emails.confirmation.greeting"),
                getMessage("emails.confirmation.message"),
                confirmationLink,
                getMessage("emails.confirmation.confirmEmail"),
                getMessage("emails.confirmation.ignore"),
                getMessage("emails.confirmation.regards"),
                getMessage("emails.confirmation.team")
            );
        sendEmail(email, subject, htmlContent);
    }
}
