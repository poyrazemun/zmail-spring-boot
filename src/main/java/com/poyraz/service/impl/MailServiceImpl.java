package com.poyraz.service.impl;

import com.poyraz.dto.AttachmentDTO;
import com.poyraz.dto.MailDTO;
import com.poyraz.service.AsyncMailService;
import com.poyraz.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
    private final Configuration templateConfig;
    private final AsyncMailService asyncMailService;

    public MailServiceImpl(Configuration templateConfig, AsyncMailService asyncMailService) {
        this.templateConfig = templateConfig;
        this.asyncMailService = asyncMailService;
    }

    @Value("${initiated.message}")
    private String initiatedMessage;

    @Override
    public String sendBasicMail(MailDTO mailDTO) {
        logger.info("sendBasicMail called for recipient: {}", mailDTO.to());
        asyncMailService.sendBasicMail(mailDTO).thenAccept(System.out::println).exceptionally(e -> {
            logger.error("Error sending basic mail", e);
            return null;
        });
        String result = String.format(initiatedMessage, LocalDateTime.now());
        logger.info("sendBasicMail result: {}", result);
        return result;
    }


    public String sendAdvancedMail(MailDTO mailDTO, Resource attachment) {
        logger.info("sendAdvancedMail called for recipient: {}", mailDTO.to());
        String mailBody = buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplateBody = new MailDTO(
                mailDTO.to(),
                mailDTO.subject(),
                mailBody,
                mailDTO.templateName(),
                mailDTO.firstName()
        );
        asyncMailService.sendMailWithAttachment(mailWithTemplateBody, attachment).thenAccept(System.out::println)
                .exceptionally(e -> {
                    logger.error("Error sending advanced mail with attachment", e);
                    return null;
                });
        return String.format(initiatedMessage, LocalDateTime.now());
    }

    //this sendAdvancedMail is for sending emails with attachment/attachments of user's wish.
    public String sendAdvancedMail(MailDTO mailWithTemplate, List<MultipartFile> attachments) {
        List<AttachmentDTO> attachmentDataList = convertAttachments(attachments);
        asyncMailService.sendMailWithAttachment(mailWithTemplate, attachmentDataList).thenAccept(System.out::println)
                .exceptionally(e -> {
                    logger.error("Error sending advanced mail with user attachments", e);
                    return null;
                });

        return String.format(initiatedMessage, LocalDateTime.now());
    }

    private List<AttachmentDTO> convertAttachments(List<MultipartFile> attachments) {
        if (Objects.isNull(attachments)) {
            return new ArrayList<>();
        }
        return attachments.stream().map(file -> {
            try {
                return new AttachmentDTO(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            } catch (IOException e) {
                logger.error("Error converting attachment: {}", file.getOriginalFilename(), e);
                return null;
            }
        }).toList();
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
