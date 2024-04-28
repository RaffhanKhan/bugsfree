package com.bugsfree.emailsender.dto;

import lombok.Data;

@Data
public class EmailSenderDTO {

    private String[] toEmailAddress;
    private String fromEmailAddress;
    private String subject;
    private String text;
    private String firstName;
    private String secondName;
    private String visitURL;
    private String templateId;
    private String[] ccAddress;
    private String[] bccAddress;
}
