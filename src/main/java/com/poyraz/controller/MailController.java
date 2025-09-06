package com.poyraz.controller;

import com.poyraz.dto.MailDTO;
import com.poyraz.service.impl.MailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
