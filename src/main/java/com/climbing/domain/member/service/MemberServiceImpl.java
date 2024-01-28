package com.climbing.domain.member.service;

import com.climbing.domain.member.GetLoginMember;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.dto.MemberDto;
import com.climbing.domain.member.dto.MemberSignUpDto;
import com.climbing.domain.member.dto.MemberUpdateDto;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = memberSignUpDto.toEntity();
        member.authorizeUser();
        member.encodePassword(passwordEncoder);

        if (memberRepository.findByEmail(memberSignUpDto.email()).isPresent()) {
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_EMAIL);
        }

        memberRepository.save(member);
    }

    @Override
    public void update(MemberUpdateDto memberUpdateDto, String nickname) throws Exception {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        memberUpdateDto.nickname().ifPresent(member::updateNickname);
    }

    @Override
    public void updatePassword(String beforePassword, String afterPassword, String nickname) throws Exception {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!member.matchPassword(passwordEncoder, beforePassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder, afterPassword);
    }

    @Override
    public void withdraw(String checkPassword, String nickname) throws Exception {
        Member member = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!member.matchPassword(passwordEncoder, checkPassword)) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }

    @Override
    public MemberDto getInfo(Long id) throws Exception {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }

    @Override
    public MemberDto getMyInfo() throws Exception {
        Member member = memberRepository.findByNickname(GetLoginMember.getLoginMemberNickname()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberDto(member);
    }
}
