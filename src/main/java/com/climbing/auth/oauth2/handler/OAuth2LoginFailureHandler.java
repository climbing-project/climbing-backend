package com.climbing.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        if (authenticationException instanceof LockedException) {
            response.getWriter().write("비활성화된 계정입니다. 관리자에 문의해주세요.");
        } else {
            response.getWriter().write("소셜 로그인에 실패하였습니다.");
        }
        log.info("소셜 로그인에 실패하였습니다. 에러 메시지 : {}", authenticationException.getMessage());
    }
}
