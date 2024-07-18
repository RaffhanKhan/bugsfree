package com.bugsfree.emailsender.service;

import com.bugsfree.emailsender.dto.EmailSenderDTO;
import com.bugsfree.emailsender.utils.EmailSenderConstants;
import com.bugsfree.emailsender.utils.HelperUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceHolderValidatorServiceImpl implements PlaceHolderValidatorService {

    public static final Logger LOGGER = LoggerFactory.getLogger(PlaceHolderValidatorServiceImpl.class);
    @Override
    public List<String> validatePlaceHolders(EmailSenderDTO emailSenderRequestDTO) {
        List<String> mandatoryPlaceHolders = new ArrayList<>();
        String templateId = emailSenderRequestDTO.getTemplateId().toUpperCase();

        mandatoryPlaceHoldersCheckForID001(mandatoryPlaceHolders, templateId, emailSenderRequestDTO);

        return mandatoryPlaceHolders;
    }

    private void mandatoryPlaceHoldersCheckForID001(List<String> mandatoryPlaceHolders,
                                                    String templateId, EmailSenderDTO emailSenderRequestDTO) {
        LOGGER.error(emailSenderRequestDTO.toString());
        LOGGER.error(emailSenderRequestDTO.getFirstName());
        if(HelperUtil.checksNullOrEmpty(emailSenderRequestDTO.getFirstName())){
            LOGGER.error("inside nullcheck :{}", emailSenderRequestDTO.getFirstName());
            mandatoryPlaceHolders.add(EmailSenderConstants.FIRST_NAME);
        }
    }
}
