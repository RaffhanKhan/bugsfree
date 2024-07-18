package com.bugsfree.emailsender.controller;

import com.bugsfree.emailsender.dto.EmailSenderDTO;
import com.bugsfree.emailsender.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailSenderController {

    private final EmailService emailService;

    @PostMapping("/sendemail")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailSenderDTO emailSenderDTO) {
        Map<String, Object> response = emailService.sendEmail(emailSenderDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
