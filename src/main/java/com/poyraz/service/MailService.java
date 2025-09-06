package com.poyraz.service;

import com.poyraz.dto.MailDTO;

public interface MailService {
    String sendBasicMail(MailDTO mailDTO);
}
