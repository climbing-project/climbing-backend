package com.climbing.domain.member.service;

import com.climbing.api.request.OauthJoinRequest;
import com.climbing.domain.member.dto.MemberDto;
import com.climbing.domain.member.dto.MemberJoinDto;
import com.climbing.domain.member.dto.MemberUpdateDto;
import com.climbing.global.exception.BaseException;

public interface MemberService {
    void join(MemberJoinDto memberJoinDto) throws Exception;

    void update(MemberUpdateDto memberUpdateDto, String email) throws Exception;

    void oauthJoin(OauthJoinRequest oauthJoinRequest) throws Exception;

    void updatePassword(String beforePassword, String afterPassword, String email) throws Exception;

    void setTempPassword(String email, String tempPassword);

    void withdraw(String checkPassword, String email) throws Exception;

    MemberDto getInfo(Long id) throws Exception;

    MemberDto getMyInfo() throws Exception;

    boolean checkEmail(String email) throws Exception;

    boolean checkNickname(String nickname) throws Exception;

    String findMemberEmailToNickname(String email) throws Exception;

    String findSocialType(String email) throws BaseException;
}
