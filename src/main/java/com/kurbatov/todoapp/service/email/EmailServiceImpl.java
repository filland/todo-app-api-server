package com.kurbatov.todoapp.service.email;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.service.ConfirmationTokenService;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private UserService userService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

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

    @Override
    @Transactional
    public void confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenService.findByToken(confirmationToken)
                .orElseThrow(() -> new TodoAppException(ErrorType.CONFIRMATION_TOKEN_NOT_FOUND));
        token.setActive(false);
        confirmationTokenService.save(token);

        User user = userService.findByEmail(token.getUser().getEmail())
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
        user.setEmailConfirmed(true);
        userService.saveUser(user);

    }
}
