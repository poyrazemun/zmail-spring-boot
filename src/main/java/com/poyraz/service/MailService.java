package com.poyraz.service;

import com.poyraz.dto.MailDTO;
import org.springframework.core.io.Resource;

public interface MailService {
    String sendBasicMail(MailDTO mailDTO);

    String sendAdvancedMail(MailDTO mailDTO, Resource attachment);
}
