package com.climbing.domain.member.dto;

import com.climbing.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberDto {

    private String email;
    private String nickname;

    @Builder
    public MemberDto(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
