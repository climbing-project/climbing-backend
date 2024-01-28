package com.climbing.domain.member;

import com.climbing.domain.member.dto.MemberDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetLoginMember {
    public static String getLoginMemberNickname() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = (Member) principal;
        return new MemberDto(member).getNickname();
    }
}
