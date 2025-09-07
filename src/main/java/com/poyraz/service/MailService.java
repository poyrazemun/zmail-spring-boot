package com.poyraz.service;

import com.poyraz.dto.MailDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MailService {
    String sendBasicMail(MailDTO mailDTO);

    String sendAdvancedMail(MailDTO mailDTO, Resource attachment);

    String sendAdvancedMail(MailDTO mailWithTemplate, List<MultipartFile> attachments);
}
