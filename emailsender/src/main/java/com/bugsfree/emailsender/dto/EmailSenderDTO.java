package com.bugsfree.emailsender.dto;

import lombok.Data;

@Data
public class EmailSenderDTO {

    private String toEmailAddress;
    private String fromEmailAddress;
    private String subject;
    private String text;
}
