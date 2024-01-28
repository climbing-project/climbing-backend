package com.climbing.domain.member.service;

import com.climbing.domain.member.dto.MemberDto;
import com.climbing.domain.member.dto.MemberSignUpDto;
import com.climbing.domain.member.dto.MemberUpdateDto;

public interface MemberService {
    void signUp(MemberSignUpDto memberSignUpDto) throws Exception;

    void update(MemberUpdateDto memberUpdateDto, String nickName) throws Exception;

    void updatePassword(String beforePassword, String afterPassword, String nickName) throws Exception;

    void withdraw(String checkPassword, String nickName) throws Exception;

    MemberDto getInfo(Long id) throws Exception;

    MemberDto getMyInfo() throws Exception;
}
