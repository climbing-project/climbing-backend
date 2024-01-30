package com.climbing.auth.oauth2.handler;

import com.climbing.auth.jwt.JwtService;
import com.climbing.auth.oauth2.CustomOAuth2Member;
import com.climbing.domain.member.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, SecurityException {
        log.info("OAuth2 로그인 성공");
        try {
            CustomOAuth2Member oAuth2User = (CustomOAuth2Member) authentication.getPrincipal();
            if (oAuth2User.getRole() == Role.GUEST) {
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                response.addHeader(accessHeader, "Bearer " + accessToken);
                response.sendRedirect("/member/oauth2/sign-up");
                //TODO : 프론트로 리다이렉트 보낼시 accessToken이 헤더에 추가되지 않은 현상 발생
                // 따라서 쿼리 파라미터에 담아서 리다이렉트 URL 제작과정 추가
                jwtService.sendAccessTokenAndRefreshToken(response, accessToken, null);
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(accessHeader, "Bearer " + accessToken);
        response.addHeader(refreshHeader, "Bearer " + refreshToken);

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }
}
