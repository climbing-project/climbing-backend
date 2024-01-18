package com.climbing.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberDTO {
    
    private String email;
    private String password;
    private String nickname;

}
