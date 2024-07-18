package com.bugsfree.registration.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService{


    private final Environment environment;


    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*1000*30))
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()*604800000))
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] key = Decoders.BASE64.decode(environment.getProperty("jwt.secret.key"));
        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public String extractUserName(String token) {
        return expectClaim(token, Claims::getSubject);
    }

    private <T> T expectClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = expectTillClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims expectTillClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExperired(token));
    }

    private boolean isTokenExperired(String token) {
        return expectClaim(token, Claims::getExpiration).before(new Date());
    }
}
