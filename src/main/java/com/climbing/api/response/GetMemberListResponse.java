package com.climbing.api.response;

import com.climbing.auth.oauth2.SocialType;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberListResponse {
    private Long id;
    private String email;
    private String nickName;
    private Role role;
    private SocialType socialType;

    public static GetMemberListResponse of(Member member) {
        return new GetMemberListResponse(member.getId(), member.getEmail(), member.getNickname(), member.getRole(), member.getSocialType());
    }
}
