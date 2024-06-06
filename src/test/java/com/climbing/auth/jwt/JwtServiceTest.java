package com.climbing.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.Role;
import com.climbing.domain.member.repository.MemberRepository;
import com.climbing.redis.service.RedisService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JwtServiceTest {
    @Autowired
    JwtService jwtService;
    @Autowired
    RedisService redisService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.access.header}")
    private String accessHeader;
    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "role";
    private static final String BEARER = "Bearer ";

    private final String email = "1234@1234.com";
    private final String role = Role.USER.getKey();

    @BeforeEach
    public void init() {
        Member member = Member.builder().email("1234@1234.com").password("12341234").nickname("tiger").role(Role.USER).build();
        memberRepository.save(member);
        clear();
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    private DecodedJWT getVerify(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
    }

    @Test
    @DisplayName("액세스 토큰 발급 테스트")
    public void createAccessToken() throws Exception {
        //given, when
        String accessToken = jwtService.createAccessToken(email, role);
        DecodedJWT verify = getVerify(accessToken);

        String subject = verify.getSubject();
        String findMemberEmail = verify.getClaim(EMAIL_CLAIM).asString();

        //then
        assertThat(findMemberEmail).isEqualTo(email);
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("리프레시 토큰 발급 테스트")
    public void createRefreshToken() throws Exception {
        //given, when
        String refreshToken = jwtService.createRefreshToken(email);
        DecodedJWT verify = getVerify(refreshToken);
        String subject = verify.getSubject();
        String email = verify.getClaim(EMAIL_CLAIM).asString();
        String role = verify.getClaim(ROLE_CLAIM).asString();

        //then
        assertThat(subject).isEqualTo(REFRESH_TOKEN_SUBJECT);
        assertThat(email).isNull();
        assertThat(role).isNull();
    }

    @Test
    @DisplayName("리프레시 토큰 업데이트 테스트")
    public void updateRefreshToken() throws Exception {
        //given
        String refreshToken = jwtService.createRefreshToken(email);
        jwtService.updateRefreshToken(email, refreshToken);
        clear();
        Thread.sleep(4000);

        //when
        String reCreateRefreshToken = jwtService.createRefreshToken(email);
        jwtService.updateRefreshToken(email, reCreateRefreshToken);
        clear();

        //then
        assertThat(redisService.getValues("RefreshToken" + email)).isNotEmpty();
        assertThat(redisService.getValues("RefreshToken" + email)).isEqualTo(reCreateRefreshToken);
        assertThat(refreshToken).isNotEqualTo(reCreateRefreshToken);
    }

    @Test
    @DisplayName("액세스 토큰 헤더 설정 테스트")
    public void setAccessTokenHeader() throws Exception {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email);

        jwtService.setAccessTokenHeader(mockHttpServletResponse, accessToken);

        //when
        jwtService.sendAccessTokenAndRefreshToken(mockHttpServletResponse, accessToken, refreshToken);

        //then
        String headerAccessToken = mockHttpServletResponse.getHeader(accessHeader);

        assertThat(headerAccessToken).isEqualTo(accessToken);
    }

    @Test
    @DisplayName("리프레시 토큰 헤더 설정 테스트")
    public void setRefreshTokenHeader() throws Exception {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email);

        jwtService.setRefreshTokenHeader(mockHttpServletResponse, refreshToken);

        //when
        jwtService.sendAccessTokenAndRefreshToken(mockHttpServletResponse, accessToken, refreshToken);

        //then
        String headerRefreshToken = mockHttpServletResponse.getHeader(refreshHeader);

        assertThat(headerRefreshToken).isEqualTo(refreshToken);
    }

    private HttpServletRequest setRequest(String accessToken, String refreshToken) throws IOException {

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        jwtService.sendAccessTokenAndRefreshToken(mockHttpServletResponse, accessToken, refreshToken);

        String headerAccessToken = mockHttpServletResponse.getHeader(accessHeader);
        String headerRefreshToken = mockHttpServletResponse.getHeader(refreshHeader);

        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        httpServletRequest.addHeader(accessHeader, BEARER + headerAccessToken);
        httpServletRequest.addHeader(refreshHeader, BEARER + headerRefreshToken);

        return httpServletRequest;
    }


    @Test
    @DisplayName("액세스 토큰 추출 테스트")
    public void extractAccessToken() throws Exception {
        //given
        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email);
        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);

        //when
        String extractAccessToken = jwtService.extractAccessToken(httpServletRequest).get();

        //then
        assertThat(extractAccessToken).isEqualTo(accessToken);
        assertThat(getVerify(extractAccessToken).getClaim(EMAIL_CLAIM).asString()).isEqualTo(email);

    }

    @Test
    @DisplayName("리프레시 토큰 추출 테스트")
    public void extractRefreshToken() throws Exception {
        //given
        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email);
        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);

        //when
        String extractRefreshToken = jwtService.extractRefreshToken(httpServletRequest).get();

        //then
        assertThat(extractRefreshToken).isEqualTo(refreshToken);
        assertThat(getVerify(extractRefreshToken).getSubject()).isEqualTo(REFRESH_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("이메일 추출 테스트")
    public void extractEmail() throws Exception {
        //given
        String accessToken = jwtService.createAccessToken(email, role);
        String refreshToken = jwtService.createRefreshToken(email);
        HttpServletRequest httpServletRequest = setRequest(accessToken, refreshToken);
        String requestAccessToken = jwtService.extractAccessToken(httpServletRequest).get();

        //when
        String extractEmail = jwtService.extractEmail(requestAccessToken).get();

        //then
        assertThat(extractEmail).isEqualTo(email);
    }


}
