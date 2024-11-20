package walkonmoon.fashion.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public boolean sendPasswordRecoveryEmail(String toEmail, String token) {
        try {
            String subject = "Chroma Password Recovery";
            String link = baseUrl + "/reset-password.html?token=" + token;
            String content = "<p>Hello,</p>" +
                    "<p>We received a request to reset your password.</p>" +
                    "<p><a href=\"" + link + "\">Click here to reset your password</a></p>" +
                    "<p>If you did not request this, please ignore this email.</p>";

            sendHtmlEmail(toEmail, subject, content);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            return false;
        }
    }

    public boolean sendContactResponse(String toEmail, String contactInfo) {
        try {
            String subject = "Chroma Contact Response";
            String content = "<p>Hello,</p>" +
                    "<p>We received a contact message from you.</p>" +
                    "<p>Thank you for contacting us. We will reply you as soon as possible</p>" +
                    "<p>Here is your message : <p>"+
                    "<p>"+contactInfo+"</p>"+
                    "<p>If you did not send this, please ignore this email.</p>";

            sendHtmlEmail(toEmail, subject, content);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            return false;
        }
    }


    private void sendHtmlEmail(String toEmail, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("chromashopservice@gmail.com");  // Sender email

        mailSender.send(message);
    }
}

