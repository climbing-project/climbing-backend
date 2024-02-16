package com.climbing.auth.login.handler;

import com.climbing.auth.jwt.JwtService;
import com.climbing.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String email = extractUsername(authentication);
        List<String> roleList = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            roleList.add(authority.getAuthority());
        });
        String role = roleList.getFirst();
        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);

        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    member.updateRefreshToken(refreshToken);
                    memberRepository.saveAndFlush(member);
                    if (member.isBlocked()) {
                        throw new LockedException("비활성화된 계정. DB 확인 필요");
                    }
                });
        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. 액세스 토큰 : {}", accessToken);
        log.info("액세스 토큰 만료 기간 : {}", accessTokenExpiration);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
