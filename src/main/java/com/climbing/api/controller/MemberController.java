package com.climbing.api.controller;

import com.climbing.domain.member.MemberDTO;
import com.climbing.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberDTO memberDTO) throws Exception {
        memberService.signUp(memberDTO);
        return "sign-up success";
    }

}
