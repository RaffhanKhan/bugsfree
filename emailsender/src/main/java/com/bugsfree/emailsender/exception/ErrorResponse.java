package com.bugsfree.emailsender.exception;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable {
    private String status;
    private String code;
    private String description;

    public ErrorResponse() {

    }

    public ErrorResponse(String status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

}
