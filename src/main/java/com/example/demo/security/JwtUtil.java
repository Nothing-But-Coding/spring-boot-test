package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private static final long EXPIRE_TIME = 10 * 60 * 1000; // 10 minutes

    public String createToken(String username) {
        Date now = new Date();
        Algorithm algorithm = Algorithm.HMAC512(secretKey);

        return JWT.create()
                .withSubject(username)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + EXPIRE_TIME))
                .sign(algorithm);
    }

    public String validateTokenAndRetrieveSubject(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey))
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception){
            // Log the exception details (optional)
            System.out.println("Token verification failed: " + exception.getMessage());
            // Return null or throw a custom exception
            return null;
        }
    }
}
