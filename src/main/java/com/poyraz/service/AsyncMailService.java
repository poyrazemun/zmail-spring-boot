package com.poyraz.service;

import com.poyraz.dto.AttachmentDTO;
import com.poyraz.dto.MailDTO;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncMailService {
    CompletableFuture<String> sendBasicMail(MailDTO mailDTO);

    CompletableFuture<String> sendMailWithAttachment(MailDTO mailDTO, Resource attachment);

    CompletableFuture<String> sendMailWithAttachment(MailDTO mailDTO, List<AttachmentDTO> attachments);

}
