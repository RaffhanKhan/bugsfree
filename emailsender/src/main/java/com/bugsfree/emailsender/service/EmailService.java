package com.bugsfree.emailsender.service;

import com.bugsfree.emailsender.dto.EmailSenderDTO;

import java.util.Map;

public interface EmailService {
    Map<String, Object> sendEmail(EmailSenderDTO emailSenderDTO);
}
