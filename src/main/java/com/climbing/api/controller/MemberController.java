package com.climbing.api.controller;

import com.climbing.domain.member.GetLoginMember;
import com.climbing.domain.member.dto.*;
import com.climbing.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public String signUp(@Valid @RequestBody MemberSignUpDto memberSignUpDto) throws Exception {
        memberService.signUp(memberSignUpDto);
        return "sign-up success";
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateMemberInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) throws Exception {
        String nickName = GetLoginMember.getLoginMemberNickname();
        memberService.update(memberUpdateDto, nickName);
    }

    @PostMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.beforePassword(), updatePasswordDto.afterPassword(), GetLoginMember.getLoginMemberNickname());

    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.checkPassword(), GetLoginMember.getLoginMemberNickname());
    }

    @GetMapping("/{id}")
    public ResponseEntity getInfo(@Valid @PathVariable("id") Long id) throws Exception {
        MemberDto dto = memberService.getInfo(id);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @GetMapping("/myInfo")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {
        MemberDto dto = memberService.getMyInfo();
        return new ResponseEntity(dto, HttpStatus.OK);
    }


    @GetMapping("/oauth2/sign-up") //oauth redirect url 테스트용
    public String oauthSignUp() throws Exception {
        return "sign-up success";
    }
}
