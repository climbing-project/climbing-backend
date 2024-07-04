package com.climbing.domain.member.service;

import com.climbing.api.request.AuthorizeRoleRequest;
import com.climbing.api.request.OauthJoinRequest;
import com.climbing.api.response.AuthorizeRoleResponse;
import com.climbing.api.response.GetMemberListResponse;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.dto.MemberDto;
import com.climbing.domain.member.dto.MemberJoinDto;
import com.climbing.domain.member.dto.MemberUpdateDto;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import com.climbing.global.exception.BaseException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDto memberJoinDto) throws BaseException {
        Member member = memberJoinDto.toEntity();
        member.authorizeUser();
        member.encodePassword(passwordEncoder);

        if (memberRepository.findByEmail(memberJoinDto.email()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_EMAIL);
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto, String email) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        if (memberRepository.findByNickname(memberUpdateDto.nickname()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }
        member.updateNickname(memberUpdateDto.nickname());
    }

    @Override
    public void oauthJoin(OauthJoinRequest oauthJoinRequest) throws BaseException {
        Member member = memberRepository.findByEmail(oauthJoinRequest.email()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        if (memberRepository.findByNickname(oauthJoinRequest.nickname()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_NICKNAME);
        }
        member.updateNickname(oauthJoinRequest.nickname());
    }

    @Override
    public void authorizeUser(OauthJoinRequest oauthJoinRequest) throws BaseException {
        Member member = memberRepository.findByEmail(oauthJoinRequest.email()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        member.authorizeUser();
    }

    @Override
    public void updatePassword(String beforePassword, String afterPassword, String email) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!member.matchPassword(passwordEncoder, beforePassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder, afterPassword);
    }

    @Override
    public void setTempPassword(String email, String tempPassword) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        member.updatePassword(passwordEncoder, tempPassword);
    }

    @Override
    public void withdraw(String checkPassword, String email) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }

    @Override
    public MemberDto getInfo(Long id) throws BaseException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }

    @Override
    public MemberDto getMyInfo() throws BaseException {
        Member member = memberRepository.findByEmail(GetLoginMember.getLoginMemberEmail()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }

    @Override
    public boolean checkEmail(String email) throws BaseException {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean checkNickname(String nickname) throws BaseException {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    @Override
    public String findMemberEmailToNickname(String email) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return member.getNickname();
    }

    @Override
    public String findSocialType(String email) throws BaseException {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        if (member.getSocialType() == null) {
            return "NORMAL";
        }
        return member.getSocialType().toString();
    }

    @Override
    public AuthorizeRoleResponse authorizeRole(AuthorizeRoleRequest authorizeRoleRequest, Long id) throws BaseException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        switch (authorizeRoleRequest.role()) {
            case "MANAGER" -> member.authorizeManager();
            case "ADMIN" -> member.authorizeAdmin();
            case "USER" -> member.authorizeUser();
            default -> throw new MemberException(MemberExceptionType.WRONG_ROLE);
        }
        return AuthorizeRoleResponse.of("권한 변경이 완료되었습니다.", member.getId(), String.valueOf(member.getRole()));
    }

    @Override
    public List<GetMemberListResponse> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(GetMemberListResponse::of).collect(Collectors.toList());
    }

    @Override
    public Page<GetMemberListResponse> findAllMembersPage(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(GetMemberListResponse::of);
    }

    @Override
    public Page<GetMemberListResponse> findMembersByRole(String role, Pageable pageable) {
        Page<Member> members = memberRepository.findByRole(role, pageable);
        return members.map(GetMemberListResponse::of);
    }
}
