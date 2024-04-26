package com.bugsfree.emailsender.service;

import com.bugsfree.emailsender.dto.EmailSenderDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailsender;
    private final Environment environment;

    @Override
    public Map<String, Object> sendEmail(EmailSenderDTO emailSenderDTO) {
        logger.debug("ENTER-sendEmail-with-request :{}", emailSenderDTO);

        Map<String, Object> response = new HashMap<>();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailSenderDTO.getToEmailAddress());
        message.setFrom(environment.getProperty("from.email.address"));
        message.setSubject(emailSenderDTO.getSubject());
        message.setText(emailSenderDTO.getText());
        javaMailsender.send(message);

        response.put("status", "success");
        logger.debug("EXIT-sendEmail:{}", response) ;
        return response;
    }
}
