package com.bugsfree.registration.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {

    public String generateToken(UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails);

    public String extractUserName(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);
}
