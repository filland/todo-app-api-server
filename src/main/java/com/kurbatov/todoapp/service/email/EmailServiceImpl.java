package com.kurbatov.todoapp.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    // it does not make sense because the FROM is always the same now
//    @Value("${mail.from}")
//    private String mailFrom;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    @Transactional
    public void sendRegistrationConfirmationEmail(String subject, String recipient, String confirmationLink) {

        MimeMessagePreparator preparator = mimeMessage -> {

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
            message.setSubject(subject);
            message.setTo(recipient);
//            message.setFrom(mailFrom);

            Map<String, String> params = new HashMap<>();
            params.put("confirmationLink", confirmationLink);

            String template = "/templates/email/registrationConfirmation.ftl";
            String text = templateEngine.merge(template, params);
            message.setText(text, true);

        };
        javaMailSender.send(preparator);
    }
}
