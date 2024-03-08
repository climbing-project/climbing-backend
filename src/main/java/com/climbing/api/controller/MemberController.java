package com.climbing.api.controller;

import com.climbing.api.request.EmailRequest;
import com.climbing.api.request.MemberNicknameRequest;
import com.climbing.api.request.OauthJoinRequest;
import com.climbing.auth.email.EmailAuthResponse;
import com.climbing.auth.email.EmailInfo;
import com.climbing.auth.email.service.EmailService;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.member.dto.*;
import com.climbing.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.OK)
    public void join(@Valid @RequestBody MemberJoinDto memberJoinDto) throws Exception {
        memberService.join(memberJoinDto);
        EmailInfo emailInfo = EmailInfo.builder()
                .receiver(memberJoinDto.email())
                .title("[오르리]" + memberJoinDto.nickname() + "님 가입을 진심으로 환영합니다.")
                .build();
        emailService.sendJoinEmail(emailInfo);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        String email = GetLoginMember.getLoginMemberEmail();
        memberService.update(memberUpdateDto, email);
    }

    @PutMapping("/oauth2/update")
    @ResponseStatus(HttpStatus.OK)
    public void oauthJoin(@Valid @RequestBody OauthJoinRequest oauthJoinRequest) throws Exception {
        memberService.oauthJoin(oauthJoinRequest);
        EmailInfo emailInfo = EmailInfo.builder()
                .receiver(oauthJoinRequest.email())
                .title("[오르리]" + oauthJoinRequest.nickname() + "님 가입을 진심으로 환영합니다.")
                .build();
        emailService.sendJoinEmail(emailInfo);
    }

    @PutMapping("/updatePassword")
    @PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.beforePassword(), updatePasswordDto.afterPassword(), GetLoginMember.getLoginMemberEmail());
    }

    @DeleteMapping
    @PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        String email = GetLoginMember.getLoginMemberEmail();
        memberService.withdraw(memberWithdrawDto.checkPassword(), email);
        EmailInfo emailInfo = EmailInfo.builder()
                .receiver(email)
                .title("[오르리] 회원탈퇴가 완료되었습니다.")
                .build();
        emailService.sendWithdrawEmail(emailInfo);
    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    public ResponseEntity getMyInfo() throws Exception {
        MemberDto dto = memberService.getMyInfo();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/oauth2/join") //oauth redirect url
    public void oauthSignUp() {
    }

    @PostMapping("/emailAuth")
    public ResponseEntity sendEmailAuthNum(@RequestBody EmailRequest request) {
        EmailInfo emailInfo = EmailInfo.builder()
                .receiver(request.email())
                .title("[오르리] 이메일 인증 코드 발송")
                .build();

        String authNum = emailService.sendEmail(emailInfo, "email");

        EmailAuthResponse response = new EmailAuthResponse();
        response.setAuthNum(authNum);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/tempPassword")
    public ResponseEntity sendTempPassword(@RequestBody EmailRequest request) {
        EmailInfo emailInfo = EmailInfo.builder()
                .receiver(request.email())
                .title("[오르리] 임시 비밀번호 발급")
                .build();

        emailService.sendEmail(emailInfo, "password");

        return ResponseEntity.ok().build();

//        EmailAuthResponse response = new EmailAuthResponse();
//        response.setAuthNum(authNum);
//
//        return ResponseEntity.ok(response);
    }

    @GetMapping("/emailCheck")
    public ResponseEntity<Boolean> checkEmail(@RequestBody EmailRequest request) throws Exception {
        boolean result = !memberService.checkEmail(request.email());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/nicknameCheck")
    public ResponseEntity<Boolean> checkNickname(@RequestBody MemberNicknameRequest request) throws Exception {
        boolean result = !memberService.checkNickname(request.nickname());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/logout")
    @PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return ("access-denied page");
    }

}
