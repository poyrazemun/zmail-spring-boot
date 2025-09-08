package com.poyraz.controller;

import com.poyraz.dto.MailDTO;
import com.poyraz.service.impl.MailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/mail")
@RestController
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    private final MailServiceImpl mailService;


    public MailController(MailServiceImpl mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/sendBasic")
    public ResponseEntity<String> sendSimpleMail(@RequestBody MailDTO mailDTO) {
        logger.info("sendSimpleMail endpoint called for recipient: {}", mailDTO.to());
        String response = mailService.sendBasicMail(mailDTO);
        logger.info("sendSimpleMail response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sendAdvanced")
    public ResponseEntity<String> sendAdvancedMail(@RequestBody MailDTO mailDTO) {
        logger.info("sendAdvancedMail endpoint called for recipient: {}", mailDTO.to());
        Resource attachment = new ClassPathResource("attachments/sampleAttachmentForPractice.txt");
        String mailBody = mailService.buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplate = new MailDTO(mailDTO.to(),
                mailDTO.subject(), mailBody, mailDTO.templateName(), mailDTO.firstName());
        String response = mailService.sendAdvancedMail(mailWithTemplate, attachment);
        logger.info("sendAdvancedMail response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sendWithYourAttachment")
    public ResponseEntity<String> sendWithYourAttachment(@RequestPart MailDTO mailDTO, @RequestPart(required = false) List<MultipartFile> attachments) {
        logger.info("sendWithYourAttachment endpoint called for recipient: {}", mailDTO.to());
        String mailBody = mailService.buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplate = new MailDTO(mailDTO.to(),
                mailDTO.subject(), mailBody, mailDTO.templateName(), mailDTO.firstName());
        String response = mailService.sendAdvancedMail(mailWithTemplate, attachments);
        logger.info("sendWithYourAttachment response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
