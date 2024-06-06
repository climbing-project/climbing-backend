package com.climbing.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        String targetUrl;
        if (authenticationException instanceof BadCredentialsException) {
            response.getWriter().write("이미 가입된 이메일입니다.");
            targetUrl = UriComponentsBuilder.fromUriString("http://13.125.164.197:443/error/already-exist")
                    .queryParam("email", authenticationException.getMessage())
                    .build()
                    .toUriString();
        } else if (authenticationException instanceof LockedException) {
            response.getWriter().write("비활성화된 계정입니다. 관리자에 문의해주세요.");
            targetUrl = UriComponentsBuilder.fromUriString("http://13.125.164.197:443/error/locked")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        } else {
            response.getWriter().write("소셜 로그인에 실패하였습니다.");
            targetUrl = UriComponentsBuilder.fromUriString("http://13.125.164.197:443/error")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        log.info("소셜 로그인에 실패하였습니다. 에러 메시지 : {}", authenticationException.getMessage());
    }
}
