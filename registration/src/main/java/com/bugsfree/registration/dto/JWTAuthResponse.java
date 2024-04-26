package com.bugsfree.registration.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String token;
    private String refreshToken;
}
