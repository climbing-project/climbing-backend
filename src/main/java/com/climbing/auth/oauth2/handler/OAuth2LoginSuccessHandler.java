package com.climbing.auth.oauth2.handler;

import com.climbing.auth.jwt.JwtService;
import com.climbing.auth.oauth2.CustomOAuth2Member;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.Role;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, SecurityException {
        log.info("OAuth2 로그인 성공");
        CustomOAuth2Member oAuth2User = (CustomOAuth2Member) authentication.getPrincipal();
        if (oAuth2User.getRole() == Role.GUEST) {
            Member member = memberRepository.findByEmail(oAuth2User.getEmail()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
            member.authorizeUser();
            memberRepository.saveAndFlush(member);
            String accessToken = jwtService.createAccessToken(oAuth2User.getEmail(), Role.USER.getKey());
            response.addHeader(accessHeader, "Bearer " + accessToken);
            String targetUrl = UriComponentsBuilder.fromUriString("http://13.125.164.197:443/members/oauth2/join")
                    .queryParam("email", member.getEmail())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } else {
            loginSuccess(request, response, oAuth2User);
        }
    }

    private void loginSuccess(HttpServletRequest request, HttpServletResponse response, CustomOAuth2Member oAuth2User) throws IOException {
        log.info("OAuth2.0 기존 사용자 로그인");
        Member member = memberRepository.findByEmail(oAuth2User.getEmail()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        if (member.isBlocked()) {
            throw new LockedException("비활성화된 계정. DB 확인 필요");
        }
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail(), oAuth2User.getRole().getKey());
        String refreshToken = jwtService.createRefreshToken();

        if (member.getRefreshToken() != null && jwtService.isTokenValid(refreshToken)) {
            refreshToken = member.getRefreshToken();
        }
        response.addHeader(accessHeader, "Bearer " + accessToken);
        response.addHeader(refreshHeader, "Bearer " + refreshToken);

        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

        String targetUrl = UriComponentsBuilder.fromUriString("http://13.125.164.197:443/")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

//        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
    }
}
