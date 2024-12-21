package club.management.club.features.services.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    //will be autowired from the application.yml configuration
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String emailSuperAdmin;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(emailSuperAdmin);
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }


    @Async
    public void sendAccountCreationEmail(
            String to,
            String username,
            String password,
            String subject
    ) throws MessagingException {
        String templateName = EmailTemplateName.ACCOUNT_CREATION_SUCCESS.getName();

        System.out.println("Password: " + password);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("password", password);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(emailSuperAdmin);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.addInline("fst-logo", new File("fst.jpeg"));
        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }

    @Async
    public void sendPasswordResetEmail(
            String to,
            String username,
            String newPassword,
            String subject
    ) throws MessagingException {
        String templateName = EmailTemplateName.RESET_PASSWORD.getName();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                "UTF-8"
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("newPassword", newPassword);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom(emailSuperAdmin);
        helper.setTo(to);
        helper.setSubject(subject);

        String template = templateEngine.process(templateName, context);

        helper.setText(template, true);

        mailSender.send(mimeMessage);
    }


}
