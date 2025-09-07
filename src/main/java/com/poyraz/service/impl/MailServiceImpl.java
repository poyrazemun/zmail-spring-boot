package com.poyraz.service.impl;

import com.poyraz.dto.MailDTO;
import com.poyraz.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final Configuration templateConfig;

    public MailServiceImpl(JavaMailSender mailSender, Configuration templateConfig) {
        this.mailSender = mailSender;
        this.templateConfig = templateConfig;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${success.message}")
    private String successMessage;

    @Override
    public String sendBasicMail(MailDTO mailDTO) {
        return sendEmail(mailDTO);
    }

    @Override
    public String sendAdvancedMail(MailDTO mailDTO, Resource attachment) {
        String mailBody = buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplateBody = new MailDTO(
                mailDTO.to(),
                mailDTO.subject(),
                mailBody,
                mailDTO.templateName(),
                mailDTO.firstName()
        );
        return sendMailWithAttachment(mailWithTemplateBody, attachment);
    }

    private String sendEmail(MailDTO mailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(mailDTO.to());
            message.setSubject(mailDTO.subject());
            message.setText(mailDTO.body());
            mailSender.send(message);
            return successMessage;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String sendMailWithAttachment(MailDTO mailDTO, Resource attachment) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(mailDTO.to());
            mimeMessageHelper.setSubject(mailDTO.subject());
            mimeMessageHelper.setText(mailDTO.body(), true);
            if (Objects.nonNull(attachment)) {
                mimeMessageHelper.addAttachment(Objects.requireNonNull(attachment.getFilename()), attachment);
            }
            mailSender.send(message);
            return successMessage;
        } catch (MessagingException e) {
            return e.getMessage();
        }
    }

    public String buildMailBodyWithTemplate(String firstName, String templateName) {
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("firstName", firstName);
        Writer writer = new StringWriter();
        try {
            templateConfig.getTemplate(templateName).process(dataModel, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
