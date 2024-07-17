package com.spboot.fooddelivery.jwt.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${spring.application.jwt-secret:test}")
    private String jwtSecret;

    @Value("${spring.application.jwt-expiration-milliseconds:1800000}")
    private Long jwtExpirationDate;

    @Value("${spring.application.name:test}")
    private String appName;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .claim("privileges", authentication.getAuthorities())
                .issuedAt(currentDate)
                .issuer(appName)
                .expiration(expireDate)
                .signWith(key())
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token){
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build();
            if (parser.isSigned(token)) {
                parser.parse(token);
            } else {
//                log.error("Invalid jwt - unsigned");
                return false;
            }
        } catch (Exception e) {
//            log.error("Invalid jwt", e);
            return false;
        }
        return true;

    }
}
