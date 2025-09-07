package com.poyraz.controller;

import com.poyraz.dto.MailDTO;
import com.poyraz.service.impl.MailServiceImpl;
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

    private final MailServiceImpl mailService;


    public MailController(MailServiceImpl mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/sendBasic")
    public ResponseEntity<String> sendSimpleMail(@RequestBody MailDTO mailDTO) {
        String response = mailService.sendBasicMail(mailDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sendAdvanced")
    public ResponseEntity<String> sendAdvancedMail(@RequestBody MailDTO mailDTO) {
        Resource attachment = new ClassPathResource("attachments/sampleAttachmentForPractice.txt");
        String mailBody = mailService.buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplate = new MailDTO(mailDTO.to(),
                mailDTO.subject(), mailBody, mailDTO.templateName(), mailDTO.firstName());
        String response = mailService.sendAdvancedMail(mailWithTemplate, attachment);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/sendWithYourAttachment")
    public ResponseEntity<String> sendWithYourAttachment(@RequestPart MailDTO mailDTO, @RequestPart(required = false) List<MultipartFile> attachments) {
        String mailBody = mailService.buildMailBodyWithTemplate(mailDTO.firstName(), mailDTO.templateName());
        MailDTO mailWithTemplate = new MailDTO(mailDTO.to(),
                mailDTO.subject(), mailBody, mailDTO.templateName(), mailDTO.firstName());
        String response = mailService.sendAdvancedMail(mailWithTemplate, attachments);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
