package com.climbing.api.controller;

import com.climbing.api.request.EmailRequest;
import com.climbing.api.request.MemberNicknameRequest;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.dto.MemberJoinDto;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import com.climbing.domain.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;
    ObjectMapper objectMapper = new ObjectMapper();

    private final String SIGN_UP_URL = "/members/join";
    private final String email = "1234@1234.com";
    private final String password = "123abc@!#";
    private final String nickname = "cat";
    private final String receiver = "실제 이메일 작성 (테스트시 작성)";

    private void clear() {
        em.flush();
        em.clear();
    }

    private void signUpSuccess(String data) throws Exception {
        mockMvc.perform(
                        post(SIGN_UP_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data))
                .andExpect(status().isOk());
    }

    private void signUpFail(String data) throws Exception {
        mockMvc.perform(
                        post(SIGN_UP_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data))
                .andExpect(status().isBadRequest());
    }

    @Value("${jwt.access.header}")
    private String accessHeader;

    private static final String BEARER = "Bearer ";

    private String getAccessTokenAndLogin() throws Exception {

        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        MvcResult result = mockMvc.perform(
                        post("/members/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(map)))
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getHeader(accessHeader);
    }

    @Test
    @DisplayName("회원가입 정상")
    public void signUpSucceed() throws Exception {
        //given
        MemberJoinDto memberJoinDto = new MemberJoinDto(email, password, nickname);
        String data = objectMapper.writeValueAsString(memberJoinDto);

        //when
        signUpSuccess(data);

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원가입 실패 (이메일, 패스워드, 닉네임 하나가 없음)")
    public void noDataSignUp() throws Exception {
        String noEmail = objectMapper.writeValueAsString(new MemberJoinDto(null, password, nickname));
        String noPassword = objectMapper.writeValueAsString(new MemberJoinDto(email, null, nickname));
        String noNickname = objectMapper.writeValueAsString(new MemberJoinDto(email, password, null));

        signUpFail(noEmail);
        signUpFail(noPassword);
        signUpFail(noNickname);

        assertThat(memberRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("회원정보 수정")
    public void updateMember() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);

        String accessToken = getAccessTokenAndLogin();
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", nickname + "tiger");
        String updateData = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        put("/members/update")
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateData))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member.getNickname()).isEqualTo(nickname + "tiger");
    }

    @Test
    @DisplayName("회원정보 수정 (닉네임 중복 오류)")
    public void updateMemberNicknameDuplicate() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        String data2 = objectMapper.writeValueAsString(new MemberJoinDto("12" + email, password, "tiger"));
        signUpSuccess(data);
        signUpSuccess(data2);

        String accessToken = getAccessTokenAndLogin(); //data로 로그인
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", "tiger"); //data2 와 같은 닉네임으로 변경
        String updateData = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        put("/members/update")
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateData))
                .andExpect(status().isConflict());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member.getNickname()).isEqualTo(nickname); //오류로 인해 변경안됨
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    public void changPassword() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);
        String accessToken = getAccessTokenAndLogin();

        Map<String, Object> map = new HashMap<>();
        map.put("beforePassword", password);
        map.put("afterPassword", password + "!@#");

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        put("/members/updatePassword")
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isFalse();
        assertThat(passwordEncoder.matches(password + "!@#", member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("비밀번호 수정 실패 (비밀번호 확인 실패)")
    public void changPasswordWithDifferentPassword() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);
        String accessToken = getAccessTokenAndLogin();

        Map<String, Object> map = new HashMap<>();
        map.put("beforePassword", password + "!");
        map.put("afterPassword", password + "!@#");

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        put("/members/password")
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(passwordEncoder.matches(password + "!@#", member.getPassword())).isFalse();
    }

    @Test
    @DisplayName("비밀번호 변경 실패 (올바르지 않은 형식의 비밀번호)")
    public void changWrongPassword() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);
        String accessToken = getAccessTokenAndLogin();

        Map<String, Object> map = new HashMap<>();
        map.put("beforePassword", password);
        map.put("afterPassword", "1234");

        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        put("/members/password")
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
        assertThat(passwordEncoder.matches("1234", member.getPassword())).isFalse();
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    public void withDrawMember() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);

        String accessTokenAndLogin = getAccessTokenAndLogin(); //로그인

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword", password);
        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/members")
                                .header(accessHeader, BEARER + accessTokenAndLogin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isOk());

        //then
        assertThrows(Exception.class, () -> memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
    }

    @Test
    @DisplayName("회원 탈퇴 실패 (비밀번호 확인 실패)")
    public void withDrawWithWrongPassword() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);

        String accessTokenAndLogin = getAccessTokenAndLogin(); //로그인

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword", password + "123");
        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/members")
                                .header(accessHeader, BEARER + accessTokenAndLogin)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isBadRequest());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("회원탈퇴 실패 (탈퇴 권한이 없음)")
    public void WithdrawWithNoAuthentication() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);

        String accessTokenAndLogin = getAccessTokenAndLogin(); //로그인

        Map<String, Object> map = new HashMap<>();
        map.put("checkPassword", password + "123");
        String updatePassword = objectMapper.writeValueAsString(map);

        //when
        mockMvc.perform(
                        delete("/members")
                                .header(accessHeader, BEARER + accessTokenAndLogin + "a")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatePassword))
                .andExpect(status().isNotFound());

        //then
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member).isNotNull();
    }

    @Test
    @DisplayName("나의 회원정보 조회하기")
    public void findInfo() throws Exception {
        //given
        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data);

        String accessTokenAndLogin = getAccessTokenAndLogin();

        //when
        MvcResult result = mockMvc.perform(
                        get("/members/myInfo")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header(accessHeader, BEARER + accessTokenAndLogin))
                .andExpect(status().isOk()).andReturn();

        //then
        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        assertThat(member.getEmail()).isEqualTo(map.get("email"));
        assertThat(member.getNickname()).isEqualTo(map.get("nickname"));
    }

//    @Test
//    @DisplayName("일반 회원정보 조회하기")
//    public void memberInfo() throws Exception {
//        //given
//        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
//        signUpSuccess(data);
//        String accessTokenAndLogin = getAccessTokenAndLogin();
//        Long id = memberRepository.findAll().getFirst().getId();
//
//        //when
//        MvcResult result = mockMvc.perform(
//                        get("/members/" + id)
//                                .characterEncoding(StandardCharsets.UTF_8)
//                                .header(accessHeader, BEARER + accessTokenAndLogin))
//                .andExpect(status().isOk()).andReturn();
//
//        //then
//        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
//        assertThat(member.getEmail()).isEqualTo(map.get("email"));
//        assertThat(member.getNickname()).isEqualTo(map.get("nickname"));
//    }
//
//    @Test
//    @DisplayName("없는 회원정보 조회")
//    public void notMemberInfo() throws Exception {
//        //given
//        String data = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
//        signUpSuccess(data);
//        String accessTokenAndLogin = getAccessTokenAndLogin();
//
//        //when
//        MvcResult result = mockMvc.perform(
//                        get("/members/123")
//                                .characterEncoding(StandardCharsets.UTF_8)
//                                .header(accessHeader, BEARER + accessTokenAndLogin))
//                .andExpect(status().isNotFound()).andReturn();
//
//        //then
//        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
//        assertThat(map.get("errorCode")).isEqualTo(MemberExceptionType.NOT_FOUND_MEMBER.getErrorCode());
//    }

    @Test
    @DisplayName("이메일 인증메일 발송 테스트")
    public void emailAuthTest() throws Exception {
        String data = objectMapper.writeValueAsString(new EmailRequest(receiver));

        MvcResult result = mockMvc.perform(
                        post("/members/emailAuth")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data))
                .andExpect(status().isOk()).andReturn();

        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        assertThat(map.get("authNum")).isNotNull();
        System.out.println(map.get("authNum"));
    }

    @Test
    @DisplayName("임시비밀번호 발송 및 변경 확인 테스트")
    public void tempPasswordTest() throws Exception {
        String data = objectMapper.writeValueAsString(new EmailRequest(receiver));
        String join = objectMapper.writeValueAsString(new MemberJoinDto(receiver, password, nickname));
        signUpSuccess(join);

        mockMvc.perform(
                        post("/members/tempPassword")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data))
                .andExpect(status().isOk());

//        Map<String, Object> map = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
//        Member member = memberRepository.findByEmail(receiver).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
//        String authNum = (String) map.get("authNum");
//        assertThat(passwordEncoder.matches(authNum, member.getPassword())).isTrue();
//        System.out.println(map.get("authNum"));
    }

    @Test
    @DisplayName("아이디 중복 체크 테스트 (중복된 아이디로 가입)")
    public void emailCheckTestFalse() throws Exception {
        String data1 = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data1);

        String data2 = objectMapper.writeValueAsString(new EmailRequest(email));

        MvcResult result = mockMvc.perform(
                        post("/members/emailCheck")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data2))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("false");
    }

    @Test
    @DisplayName("아이디 중복 체크 테스트 (중복되지 않은 아이디로 가입)")
    public void emailCheckTestTrue() throws Exception {
        String data1 = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data1);

        String data2 = objectMapper.writeValueAsString(new EmailRequest("123" + email));

        MvcResult result = mockMvc.perform(
                        post("/members/emailCheck")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data2))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("true");
    }

    @Test
    @DisplayName("닉네임 중복 체크 테스트 (중복된 닉네임으로 가입)")
    public void nicknameCheckTestFalse() throws Exception {
        String data1 = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data1);

        String data2 = objectMapper.writeValueAsString(new MemberNicknameRequest(nickname));

        MvcResult result = mockMvc.perform(
                        post("/members/nicknameCheck")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data2))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("false");
    }

    @Test
    @DisplayName("닉네임 중복 체크 테스트 (중복되지 않은 닉네임으로 가입)")
    public void nicknameCheckTestTrue() throws Exception {
        String data1 = objectMapper.writeValueAsString(new MemberJoinDto(email, password, nickname));
        signUpSuccess(data1);

        String data2 = objectMapper.writeValueAsString(new MemberNicknameRequest("tiger"));

        MvcResult result = mockMvc.perform(
                        post("/members/nicknameCheck")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data2))
                .andExpect(status().isOk()).andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("true");
    }
}
