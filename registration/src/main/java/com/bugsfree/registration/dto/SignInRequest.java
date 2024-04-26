package com.bugsfree.registration.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String emailAddress;
    private String password;
}
