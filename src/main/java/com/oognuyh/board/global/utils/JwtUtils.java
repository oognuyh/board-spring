package com.oognuyh.board.global.utils;

import java.time.Instant;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oognuyh.board.global.config.security.UserDetailsImpl;
import com.oognuyh.board.global.config.security.VerifiedResult;

public class JwtUtils {
    private static final String SECRET_KEY = "board";
    private static final long EXPIRATION_TIME = 60 * 20;
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);
    public static final String REFRESH_TOKEN_HEADER = "x-refresh-token";
    public static final String AUTH_TOKEN_HEADER = "x-auth-token";
    public static final String PREFIX = "Bearer ";

    public static String generateToken(UserDetailsImpl userDetails) {
        return JWT.create()
            .withSubject(userDetails.getEmail())
            .withClaim("exp", Instant.now().getEpochSecond() + EXPIRATION_TIME)
            .sign(ALGORITHM);
    }

    
    public static String refreshToken(UserDetailsImpl userDetails) {
        return JWT.create()
            .withSubject(userDetails.getEmail())
            .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
            .sign(ALGORITHM);
    }

    public static VerifiedResult verify(String token) {
        try {
            DecodedJWT verfiedToken = JWT.require(ALGORITHM).build().verify(token);

            return VerifiedResult.builder()
                .email(verfiedToken.getSubject())
                .isSuccessful(true)
                .build();
        } catch (Exception e) {
            DecodedJWT decodedToken = JWT.decode(token);

            return VerifiedResult.builder()
                .email(decodedToken.getSubject())
                .isSuccessful(false)
                .build();
        }
    }
}