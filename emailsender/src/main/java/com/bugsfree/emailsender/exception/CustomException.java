package com.bugsfree.emailsender.exception;

import lombok.Data;

@Data
public class CustomException extends RuntimeException{

    private static final long serialVersionID = 1L;

    private ErrorResponse errorResponse;

    public  CustomException() {
        super();
    }

    public CustomException(ErrorResponse errorResponse){
        super();
        this.errorResponse = errorResponse;
    }
    public CustomException(ErrorResponse errorResponse, Throwable cause){
        super(cause);
        this.errorResponse = errorResponse;
    }

}
