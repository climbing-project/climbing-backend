package com.climbing.auth.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface JwtService {
    String createAccessToken(String email, String role);

    String createRefreshToken(String email);

    void updateRefreshToken(String email, String refreshToken);

    void sendAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);

    void sendAccessToken(HttpServletResponse response, String accessToken);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    Optional<String> extractEmail(String token);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);
}
