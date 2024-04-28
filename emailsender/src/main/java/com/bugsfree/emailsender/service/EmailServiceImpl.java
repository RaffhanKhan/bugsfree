package com.bugsfree.emailsender.service;

import com.bugsfree.emailsender.dto.EmailSenderDTO;
import com.bugsfree.emailsender.exception.CustomException;
import com.bugsfree.emailsender.exception.ErrorResponse;
import com.bugsfree.emailsender.utils.EmailSenderConstants;
import com.bugsfree.emailsender.utils.HelperUtil;
import com.bugsfree.emailsender.utils.PropertyUtils;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailsender;
    private final Environment environment;
    private final PlaceHolderValidatorService placeHolderValidatorService;
    private final VelocityEngine velocityEngine;
    private final VelocityContext velocityContext;
    private final JavaMailSender javaMailSender;

    private Map<String, String> templateIdMatchMap = new HashMap<>();

    @PostConstruct
    public void init() {
        setTemplateIdMatchMap();
    }

    private void setTemplateIdMatchMap() {
        templateIdMatchMap.put(EmailSenderConstants.ID001, EmailSenderConstants.ID001);
        templateIdMatchMap.put(EmailSenderConstants.ID002, EmailSenderConstants.ID002);
    }

    public Map<String, Object> sendEmailWithTemplate(EmailSenderDTO emailSenderDTO) {
        logger.debug("ENTER-sendEmail-with-request :{}", emailSenderDTO);

        Map<String, Object> response = new HashMap<>();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailSenderDTO.getToEmailAddress());
            mimeMessageHelper.setFrom(environment.getProperty("from.email.address"));
            mimeMessageHelper.setSubject(emailSenderDTO.getSubject());
            mimeMessageHelper.setText(emailSenderDTO.getText());
            javaMailSender.send(mimeMessage);
            response.put("status", "success");
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            throw new CustomException(new ErrorResponse(
                    EmailSenderConstants.ERROR, "1006", "Failed to send email"
            ));
        }
        logger.debug("EXIT-sendEmail:{}", response);
        return response;
    }

    @Override
    public Map<String, Object> sendEmail(EmailSenderDTO emailSenderDTO) {
        logger.error("ENTER-sendEmail-with-request :{}", emailSenderDTO);

        Map<String, Object> response = new HashMap<>();

        checkTemplateId(emailSenderDTO);
        String templateId = emailSenderDTO.getTemplateId().toUpperCase();

        List<String> mandatoryPlaceHolders = Collections.emptyList();
        if (templateId.equalsIgnoreCase(EmailSenderConstants.ID001) || templateId.equalsIgnoreCase(EmailSenderConstants.ID002)) {
            mandatoryPlaceHolders = placeHolderValidatorService.validatePlaceHolders(emailSenderDTO);
        } else {
            logger.error("sendEmail-The given template id invalid {}", templateId);
            throw new CustomException(new ErrorResponse(
                    EmailSenderConstants.ERROR, "1004", "No template found"
            ));
        }

        throwErrorIfMandatoryFiledMissed(response, mandatoryPlaceHolders);

        Map<String, String> replaceParams = populatePlaceHoldersForAllTemplates(emailSenderDTO);
        String subject = environment.getProperty(templateId + environment.getProperty("mail_subject"));

        populateAndSendEmail(emailSenderDTO, replaceParams, subject);

        response.put(EmailSenderConstants.PAYLOAD, new ErrorResponse(EmailSenderConstants.SUCCESS, "1000", "mailsent"));
        return response;
    }

    private void checkTemplateId(EmailSenderDTO emailSenderDTO) {
        if (HelperUtil.checksNullOrEmpty(emailSenderDTO.getTemplateId())) {
            throw new CustomException(new ErrorResponse(
                    EmailSenderConstants.ERROR, "1002", "No email Template found"
            ));
        }
    }

    private void throwErrorIfMandatoryFiledMissed(Map<String, Object> response, List<String> mandatoryPlaceHolders) {
        if (!mandatoryPlaceHolders.isEmpty()) {
            throw new CustomException(new ErrorResponse(
                    EmailSenderConstants.ERROR, "1005", "mandatory fields missing"
            ));
        }
    }

    private Map<String, String> populatePlaceHoldersForAllTemplates(EmailSenderDTO emailSenderDTO) {
        logger.debug("ENTER-populatePlaceHoldersForAllTemplates :{}", emailSenderDTO);

        Map<String, String> replaceParams = new HashMap<>();
        replaceParams.put(EmailSenderConstants.EMAIL_ADDRESS, emailSenderDTO.getFromEmailAddress());
        replaceParams.put(EmailSenderConstants.FIRST_NAME, emailSenderDTO.getFirstName());
        replaceParams.put(EmailSenderConstants.SECOND_NAME, emailSenderDTO.getSecondName());
        replaceParams.put(EmailSenderConstants.VISIT_URL, emailSenderDTO.getVisitURL());

        logger.debug("EXIT-populatePlaceHoldersForAllTemplates :{}", emailSenderDTO);
        return replaceParams;
    }

    private void populateAndSendEmail(EmailSenderDTO emailSenderDTO, Map<String, String> replaceParams, String subject) {
        String htmlContent = getHTMLEmailTemplate(emailSenderDTO, replaceParams);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailSenderDTO.getToEmailAddress());
            mimeMessageHelper.setText(htmlContent, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(environment.getProperty("from.email.address"));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage());
            throw new CustomException(new ErrorResponse(
                    EmailSenderConstants.ERROR, "1006", "Failed to send email"
            ));
        }
    }

    private String getHTMLEmailTemplate(EmailSenderDTO emailSenderRequestDTO, Map<String, String> replaceParams) {
        logger.error("ENTER-getHTMLEmailTemplate");
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        logger.error("class:{}", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        String templateId = emailSenderRequestDTO.getTemplateId().toUpperCase();
        Template template = velocityEngine.getTemplate("emailTemplate.vm");

        String messageHeader = PropertyUtils.getProperty(templateId + environment.getProperty("mail_message_header"));
        String subject = PropertyUtils.getProperty(templateId + environment.getProperty("mail_subject"));
        String body = PropertyUtils.getProperty(templateId + environment.getProperty("mail_body"));
        String webServerUrl = PropertyUtils.getProperty("web_server_url");

        StringSubstitutor substitutor = new StringSubstitutor(replaceParams);
        String mailSubject = substitutor.replace(subject);
        String mailMessageHeader = substitutor.replace(messageHeader);
        String mailBody= substitutor.replace(body);

        velocityContext.put("EMAIL_HEADER", mailSubject);
        velocityContext.put("MESSAGE_HEADER", mailMessageHeader);
        velocityContext.put("MESSAGE_BODY", mailBody);
        velocityContext.put("WEB_SERVER_URL", webServerUrl);

        StringWriter stringWriter =  new StringWriter();
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();
    }

}
