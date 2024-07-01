package com.climbing.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeRoleResponse {
    private String message;
    private Long memberId;
    private String role;

    public static AuthorizeRoleResponse of(String message, Long memberId, String role) {
        return new AuthorizeRoleResponse(message, memberId, role);
    }
}
