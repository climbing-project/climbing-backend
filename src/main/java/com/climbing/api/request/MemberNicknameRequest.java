package com.climbing.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberNicknameRequest(
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(min = 2, message = "닉네임이 너무 짧습니다. 최소 2글자이어야 합니다.") String nickname) {
}
