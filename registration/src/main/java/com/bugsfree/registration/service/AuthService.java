package com.bugsfree.registration.service;

import com.bugsfree.registration.dto.JWTAuthResponse;
import com.bugsfree.registration.dto.RefreshTokenRequest;
import com.bugsfree.registration.dto.SignInRequest;
import com.bugsfree.registration.dto.SignUpRequest;
import com.bugsfree.registration.model.User;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);

    JWTAuthResponse signIn(SignInRequest signInRequest);

    JWTAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
