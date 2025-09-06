package com.poyraz.service.impl;

import com.poyraz.dto.MailDTO;
import com.poyraz.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${success.message}")
    private String successMessage;

    @Override
    public String sendBasicMail(MailDTO mailDTO) {
        return sendEmail(mailDTO);
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
}
