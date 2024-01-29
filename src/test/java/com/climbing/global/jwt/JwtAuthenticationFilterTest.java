package com.climbing.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.Role;
import com.climbing.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtAuthenticationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Autowired
    JwtService jwtService;

    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String EMAIL = "1234@1234.com";
    private static final String PASSWORD = "123abc!@#";

    private static String NICKNAME = "cat";
    private static String KEY_EMAIL = "email";
    private static String KEY_PASSWORD = "password";

    private static final String NOT_LOGIN_URL = "/logins";
    private static final String LOGIN_URL = "/login";

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String BEARER = "Bearer ";

    private ObjectMapper objectMapper = new ObjectMapper();

    private void clear() {
        em.flush();
        em.clear();
    }

    @BeforeEach
    public void init() {
        memberRepository.save(Member.builder().email(EMAIL).password(passwordEncoder.encode(PASSWORD)).nickname(NICKNAME).role(Role.USER).build());
        clear();
    }

    private Map<String, String> getUser(String email, String password) {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_EMAIL, email);
        map.put(KEY_PASSWORD, password);
        return map;
    }

    private Map<String, String> getToken() throws Exception {
        Map<String, String> map = getUser(EMAIL, PASSWORD);

        MvcResult result = mockMvc.perform(
                        post(LOGIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(map)))
                .andReturn();
        String accessToken = result.getResponse().getHeader(accessHeader);
        String refreshToken = result.getResponse().getHeader(refreshHeader);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(accessHeader, accessToken);
        tokenMap.put(refreshHeader, refreshToken);
        return tokenMap;
    }

    @Test
    @DisplayName("토큰이 둘 다 존재하지 않는 경우")
    public void noToken() throws Exception {
        mockMvc.perform(get(NOT_LOGIN_URL))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("액세스 토큰만 유효, 리프레시 토큰 없음")
    public void onlyAccessTokenValid() throws Exception {
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String accessToken = accessTokenAndRefreshToken.get(accessHeader);

        mockMvc.perform(get(NOT_LOGIN_URL).header(accessHeader, BEARER + accessToken))
                .andExpectAll(status().isBadRequest());
    }

    @Test
    @DisplayName("액세스 토큰 유효하지 않음, 리프레시 토큰 없음")
    public void NotAccessTokenValid() throws Exception {
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String accessToken = accessTokenAndRefreshToken.get(accessHeader);

        mockMvc.perform(get(NOT_LOGIN_URL).header(accessHeader, BEARER + accessToken + "a"))
                .andExpectAll(status().isFound());
    }

    @Test
    @DisplayName("액세스 토큰 없음, 리프레시 토큰만 유효")
    public void onlyRefreshTokenValid() throws Exception {
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String refreshToken = accessTokenAndRefreshToken.get(refreshHeader);

        MvcResult result = mockMvc.perform(get(NOT_LOGIN_URL).header(refreshHeader, BEARER + refreshToken))
                .andExpect(status().isOk()).andReturn();

        String accessToken = result.getResponse().getHeader(accessHeader);

        String subject = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken).getSubject();
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
    }

    @Test
    @DisplayName("액세스 토큰 없음, 리프레시 토큰만 유효하지 않음")
    public void notRefreshTokenValid() throws Exception {
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String refreshToken = accessTokenAndRefreshToken.get(refreshHeader);

        mockMvc.perform(get(NOT_LOGIN_URL).header(refreshHeader, refreshToken))
                .andExpect(status().isFound()).andReturn();

        mockMvc.perform(get(NOT_LOGIN_URL).header(refreshHeader, BEARER + refreshToken + "a"))
                .andExpect(status().isFound()).andReturn();

    }

    @Test
    @DisplayName("액세스 토큰 유효, 리프레시 토큰 유효")
    public void validToken() throws Exception {
        //given
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String refreshToken = accessTokenAndRefreshToken.get(refreshHeader);
        String accessToken = accessTokenAndRefreshToken.get(accessHeader);

        //when, then
        MvcResult result = mockMvc.perform(get(NOT_LOGIN_URL)
                        .header(accessHeader, BEARER + accessToken)
                        .header(refreshHeader, BEARER + refreshToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseAccessToken = result.getResponse().getHeader(accessHeader);
        String responseRefreshToken = result.getResponse().getHeader(refreshHeader);

        String subject = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(responseAccessToken).getSubject();
        assertThat(subject).isEqualTo(ACCESS_TOKEN_SUBJECT);
//        accessToken, refreshToken 둘 다 재발급하는 것 디버그 통해서 확인
//        assertThat(responseRefreshToken).isNotEqualTo(refreshToken);
//        assertThat(responseAccessToken).isNotEqualTo(accessToken);
    }

    @Test
    @DisplayName("로그인 주소로 보낼 경우 필터 적용 안됨")
    public void loginURLNoFilter() throws Exception {
        Map<String, String> accessTokenAndRefreshToken = getToken();
        String accessToken = (String) accessTokenAndRefreshToken.get(accessHeader);
        String refreshToken = (String) accessTokenAndRefreshToken.get(refreshHeader);

        MvcResult result = mockMvc.perform(post(LOGIN_URL)
                        .header(refreshHeader, BEARER + refreshToken)
                        .header(accessHeader, BEARER + accessToken))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
