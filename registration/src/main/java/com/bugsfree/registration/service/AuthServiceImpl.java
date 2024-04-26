package com.bugsfree.registration.service;

import com.bugsfree.registration.dto.JWTAuthResponse;
import com.bugsfree.registration.dto.RefreshTokenRequest;
import com.bugsfree.registration.dto.SignInRequest;
import com.bugsfree.registration.dto.SignUpRequest;
import com.bugsfree.registration.model.Role;
import com.bugsfree.registration.model.User;
import com.bugsfree.registration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmailAddress(signUpRequest.getEmailAddress());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
        return user;

    }

    @Override
    public JWTAuthResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmailAddress(), signInRequest.getPassword()));

        var user  = userRepository.findByEmailAddress(
                signInRequest.getEmailAddress())
                .orElseThrow(() ->  new UsernameNotFoundException("Invalid user or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(jwt);
        jwtAuthResponse.setRefreshToken(refreshToken);

        logger.info("signIn-jwtAuthResponse:{}",jwtAuthResponse);
        return jwtAuthResponse;
    }

    @Override
    public JWTAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String emailAddress = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmailAddress(emailAddress).orElseThrow();

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
            jwtAuthResponse.setToken(jwt);
            jwtAuthResponse.setRefreshToken(refreshTokenRequest.getToken());

            logger.info("signIn-jwtAuthResponse:{}",jwtAuthResponse);
            return jwtAuthResponse;
        }
        return null;

    }
}
