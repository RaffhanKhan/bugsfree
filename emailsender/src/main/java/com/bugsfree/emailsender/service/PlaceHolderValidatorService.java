package com.bugsfree.emailsender.service;

import com.bugsfree.emailsender.dto.EmailSenderDTO;

import java.util.List;

public interface PlaceHolderValidatorService {
    List<String> validatePlaceHolders(EmailSenderDTO emailSenderRequestDTO);
}
