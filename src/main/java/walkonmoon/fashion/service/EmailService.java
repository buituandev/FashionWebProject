package walkonmoon.fashion.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringEscapeUtils;
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

//    public boolean sendPasswordRecoveryEmail(String toEmail, String token) {
//        try {
//            String subject = "Chroma Password Recovery";
//            String link = baseUrl + "/reset-password.html?token=" + token;
//            String content = "<p>Hello,</p>" +
//                    "<p>We received a request to reset your password.</p>" +
//                    "<p><a href=\"" + link + "\">Click here to reset your password</a></p>" +
//                    "<p>If you did not request this, please ignore this email.</p>";
//
//            sendHtmlEmail(toEmail, subject, content);
//            return true;
//        } catch (MessagingException e) {
//            logger.error("Failed to send email: {}", e.getMessage());
//            return false;
//        }
//    }

    public boolean sendPasswordRecoveryEmail(String toEmail, String token) {
        try {
            String subject = "Chroma Password Recovery";
            String link = baseUrl + "/reset-password.html?token=" + token;
            String content = String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body {
                                font-family: Arial, sans-serif;
                                margin: 0;
                                padding: 0;
                                background-color: #f7f7f7;
                                color: #333333;
                            }
                            .email-container {
                                max-width: 600px;
                                margin: 20px auto;
                                background-color: #ffffff;
                                border: 1px solid #dddddd;
                                border-radius: 8px;
                                overflow: hidden;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            }
                            .email-header {
                                background-color: #cc2121;
                                color: #ffffff;
                                text-align: center;
                                padding: 20px 15px;
                            }
                            .email-header h1 {
                                margin: 0;
                                font-size: 24px;
                            }
                            .email-body {
                                padding: 20px;
                            }
                            .email-body p {
                                line-height: 1.6;
                                margin: 10px 0;
                            }
                            .email-body a {
                                display: inline-block;
                                margin: 20px 0;
                                padding: 10px 20px;
                                color: #ffffff;
                                background-color: #cc2121;
                                text-decoration: none;
                                border-radius: 5px;
                                font-weight: bold;
                            }
                            .email-body a:hover {
                                background-color: #a51a1a;
                            }
                            .email-footer {
                                background-color: #f7f7f7;
                                text-align: center;
                                padding: 15px 20px;
                                font-size: 14px;
                                color: #777777;
                            }
                            .email-footer a {
                                color: #cc2121;
                                text-decoration: none;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="email-container">
                            <div class="email-header">
                                <h1>Password Recovery</h1>
                            </div>
                            <div class="email-body">
                                <p>Hello,</p>
                                <p>We received a request to reset your password. If this was you, click the button below to reset your password:</p>
                                <p style="text-align: center;">
                                    <a href="%s">Reset Password</a>
                                </p>
                                <p>If you did not request this, please ignore this email. Your account is safe.</p>
                            </div>
                            <div class="email-footer">
                                <p>&copy; 2024 Chroma. All rights reserved.</p>
                                <p><a href="#">Contact Support</a> | <a href="#">Privacy Policy</a></p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """, link);

            sendHtmlEmail(toEmail, subject, content);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            return false;
        }
    }

//    public boolean sendContactResponse(String toEmail, String contactInfo) {
//        try {
//            String subject = "Chroma Contact Response";
//            String content = "<p>Hello,</p>" +
//                    "<p>We received a contact message from you.</p>" +
//                    "<p>Thank you for contacting us. We will reply you as soon as possible</p>" +
//                    "<p>Here is your message : <p>"+
//                    "<p>"+contactInfo+"</p>"+
//                    "<p>If you did not send this, please ignore this email.</p>";
//
//            sendHtmlEmail(toEmail, subject, content);
//            return true;
//        } catch (MessagingException e) {
//            logger.error("Failed to send email: {}", e.getMessage());
//            return false;
//        }
//    }

//    public boolean sendContactResponse(String toEmail, String message) {
//        try {
//            String subject = "Chroma Contact Response";
//            String content = """
//                    <!DOCTYPE html>
//                    <html>
//                    <head>
//                        <style>
//                            body {
//                                font-family: Arial, sans-serif;
//                                margin: 0;
//                                padding: 0;
//                                background-color: #f7f7f7;
//                                color: #333333;
//                            }
//                            .email-container {
//                                max-width: 600px;
//                                margin: 20px auto;
//                                background-color: #ffffff;
//                                border: 1px solid #dddddd;
//                                border-radius: 8px;
//                                overflow: hidden;
//                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
//                            }
//                            .email-header {
//                                background-color: #cc2121;
//                                color: #ffffff;
//                                text-align: center;
//                                padding: 20px 15px;
//                            }
//                            .email-header h1 {
//                                margin: 0;
//                                font-size: 24px;
//                            }
//                            .email-body {
//                                padding: 20px;
//                            }
//                            .email-body p {
//                                line-height: 1.6;
//                                margin: 10px 0;
//                            }
//                            .email-body img {
//                                max-width: 100%;
//                                height: auto;
//                                display: block;
//                                margin: 20px auto;
//                                border-radius: 8px;
//                            }
//                            .email-footer {
//                                background-color: #f7f7f7;
//                                text-align: center;
//                                padding: 15px 20px;
//                                font-size: 14px;
//                                color: #777777;
//                            }
//                            .email-footer a {
//                                color: #cc2121;
//                                text-decoration: none;
//                            }
//                        </style>
//                    </head>
//                    <body>
//                        <div class="email-container">
//                            <div class="email-header">
//                                <h1>Thank You for Contacting Us</h1>
//                            </div>
//                            <div class="email-body">
//                                <p>Hello,</p>
//                                <p>We received a contact message from you. Thank you for reaching out to us. We will get back to you as soon as possible.</p>
//                                <p>Here is your message:</p>
//                                <div style="background-color: #f9f9f9; padding: 15px; border-left: 5px solid #cc2121; border-radius: 5px;">
//                                    <p style="margin: 0;">%s</p>
//                                </div>
//                                <img src="https://i.pinimg.com/originals/73/d5/3a/73d53a4544535719da92cc6ba2722332.gif" alt="Thank You Image">
//                                <p>If you did not send this message, please ignore this email.</p>
//                                <p>Need more help? Visit our <a href="#" style="color: #cc2121;">Help Center</a> or reply directly to this email.</p>
//                            </div>
//                            <div class="email-footer">
//                                <p>&copy; 2024 Chroma. All rights reserved.</p>
//                                <p><a href="#">Unsubscribe</a> | <a href="#">Privacy Policy</a></p>
//                            </div>
//                        </div>
//                    </body>
//                    </html>
//                    """;
//            String htmlContent = String.format(content,message);
//
//            sendHtmlEmail(toEmail, subject, htmlContent);
//            return true;
//        } catch (MessagingException e) {
//            logger.error("Failed to send email: {}", e.getMessage());
//            return false;
//        }
//    }
public boolean sendContactResponse(String toEmail, String message) {
    try {
        String subject = "Chroma Contact Response";
        String content = """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            background-color: #f7f7f7;
                            color: #333333;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border: 1px solid #dddddd;
                            border-radius: 8px;
                            overflow: hidden;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        }
                        .email-header {
                            background-color: #cc2121;
                            color: #ffffff;
                            text-align: center;
                            padding: 20px 15px;
                        }
                        .email-header h1 {
                            margin: 0;
                            font-size: 24px;
                        }
                        .email-body {
                            padding: 20px;
                        }
                        .email-body p {
                            line-height: 1.6;
                            margin: 10px 0;
                        }
                        .email-body img {
                            max-width: 100%%;
                            height: auto;
                            display: block;
                            margin: 20px auto;
                            border-radius: 8px;
                        }
                        .email-footer {
                            background-color: #f7f7f7;
                            text-align: center;
                            padding: 15px 20px;
                            font-size: 14px;
                            color: #777777;
                        }
                        .email-footer a {
                            color: #cc2121;
                            text-decoration: none;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="email-header">
                            <h1>Thank You for Contacting Us</h1>
                        </div>
                        <div class="email-body">
                            <p>Hello,</p>
                            <p>We received a contact message from you. Thank you for reaching out to us. We will get back to you as soon as possible.</p>
                            <p>Here is your message:</p>
                            <div style="background-color: #f9f9f9; padding: 15px; border-left: 5px solid #cc2121; border-radius: 5px;">
                                <p style="margin: 0;">%s</p>
                            </div>
                            <img src="https://i.pinimg.com/originals/73/d5/3a/73d53a4544535719da92cc6ba2722332.gif" alt="Thank You Image">
                            <p>If you did not send this message, please ignore this email.</p>
                            <p>Need more help? Visit our <a href="#" style="color: #cc2121;">Help Center</a> or reply directly to this email.</p>
                        </div>
                        <div class="email-footer">
                            <p>&copy; 2024 Chroma. All rights reserved.</p>
                            <p><a href="#">Unsubscribe</a> | <a href="#">Privacy Policy</a></p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        // Note the double %% for escaping `%` in CSS styles.
        String htmlContent = String.format(content, message);

        sendHtmlEmail(toEmail, subject, htmlContent);
        return true;
    } catch (MessagingException e) {
        logger.error("Failed to send email: {}", e.getMessage());
        return false;
    }
}

//public boolean sendContactResponse(String toEmail, String message) {
//    try {
//        String subject = "Chroma Contact Response";
//        String sanitizedMessage = StringEscapeUtils.escapeHtml4(message);
//        String content = String.format("""
//    <!DOCTYPE html>
//    <html>
//    <head>
//        <style>
//            body {
//                font-family: Arial, sans-serif;
//                margin: 0;
//                padding: 0;
//                background-color: #f7f7f7;
//                color: #333333;
//            }
//            .email-container {
//                max-width: 600px;
//                margin: 20px auto;
//                background-color: #ffffff;
//                border: 1px solid #dddddd;
//                border-radius: 8px;
//                overflow: hidden;
//                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
//            }
//            .email-header {
//                background-color: #cc2121;
//                color: #ffffff;
//                text-align: center;
//                padding: 20px 15px;
//            }
//            .email-header h1 {
//                margin: 0;
//                font-size: 24px;
//            }
//            .email-body {
//                padding: 20px;
//            }
//            .email-body p {
//                line-height: 1.6;
//                margin: 10px 0;
//            }
//            .email-body img {
//                max-width: 100%;
//                height: auto;
//                display: block;
//                margin: 20px auto;
//                border-radius: 8px;
//            }
//            .email-footer {
//                background-color: #f7f7f7;
//                text-align: center;
//                padding: 15px 20px;
//                font-size: 14px;
//                color: #777777;
//            }
//            .email-footer a {
//                color: #cc2121;
//                text-decoration: none;
//            }
//        </style>
//    </head>
//    <body>
//        <div class="email-container">
//            <div class="email-header">
//                <h1>Thank You for Contacting Us</h1>
//            </div>
//            <div class="email-body">
//                <p>Hello,</p>
//                <p>We received a contact message from you. Thank you for reaching out to us. We will get back to you as soon as possible.</p>
//                <p>Here is your message:</p>
//                <div style="background-color: #f9f9f9; padding: 15px; border-left: 5px solid #cc2121; border-radius: 5px;">
//                    <p style="margin: 0;">%s</p>
//                </div>
//                <img src="https://i.pinimg.com/originals/73/d5/3a/73d53a4544535719da92cc6ba2722332.gif" alt="Thank You Image">
//                <p>If you did not send this message, please ignore this email.</p>
//                <p>Need more help? Visit our <a href="#" style="color: #cc2121;">Help Center</a> or reply directly to this email.</p>
//            </div>
//            <div class="email-footer">
//                <p>&copy; 2024 Chroma. All rights reserved.</p>
//                <p><a href="#">Unsubscribe</a> | <a href="#">Privacy Policy</a></p>
//            </div>
//        </div>
//    </body>
//    </html>
//    """, sanitizedMessage);
//
//        sendHtmlEmail(toEmail, subject, content);
//        return true;
//    } catch (MessagingException e) {
//        logger.error("Failed to send email", e);
//        return false;
//    }
//}



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

