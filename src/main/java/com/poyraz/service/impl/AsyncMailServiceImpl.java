package com.poyraz.service.impl;

import com.poyraz.dto.AttachmentDTO;
import com.poyraz.dto.MailDTO;
import com.poyraz.service.AsyncMailService;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncMailServiceImpl implements AsyncMailService {
    private static final Logger logger = LoggerFactory.getLogger(AsyncMailServiceImpl.class);
    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${success.message}")
    private String successMessage;

    public AsyncMailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;

    }

    @Async
    @Override
    public CompletableFuture<String> sendBasicMail(MailDTO mailDTO) {
        logger.info("Async sendBasicMail started for recipient: {}", mailDTO.to());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(mailDTO.to());
            message.setSubject(mailDTO.subject());
            message.setText(mailDTO.body());
            mailSender.send(message);
            logger.info("Async sendBasicMail sent successfully to: {}", mailDTO.to());
            return CompletableFuture.completedFuture(String.format(successMessage, LocalDateTime.now()));
        } catch (Exception e) {
            logger.error("Error in async sendBasicMail for recipient: {}", mailDTO.to(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<String> sendMailWithAttachment(MailDTO mailDTO, Resource attachment) {
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
            return CompletableFuture.completedFuture(String.format(successMessage, LocalDateTime.now()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    @Override
    public CompletableFuture<String> sendMailWithAttachment(MailDTO mailDTO, List<AttachmentDTO> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(mailDTO.to());
            mimeMessageHelper.setSubject(mailDTO.subject());
            mimeMessageHelper.setText(mailDTO.body(), true);
            if (!attachments.isEmpty()) {
                for (AttachmentDTO attachment : attachments) {
                    if (Objects.nonNull(attachment)) {
                        DataSource dataSource = new ByteArrayDataSource(attachment.data(), attachment.contentType());
                        mimeMessageHelper.addAttachment(attachment.fileName(), dataSource);
                    }
                }
            }
            mailSender.send(message);
            return CompletableFuture.completedFuture(String.format(successMessage, LocalDateTime.now()));
        } catch (MessagingException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}
