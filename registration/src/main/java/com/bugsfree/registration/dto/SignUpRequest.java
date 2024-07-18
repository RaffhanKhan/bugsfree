package com.bugsfree.registration.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignUpRequest {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
}
