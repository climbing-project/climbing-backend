package com.climbing.api.chat;

import lombok.Getter;

@Getter
public enum ChatErrorCode {
    INVALID_MESSAGE(604, "CHAT-001", "메시지가 유효하지 않습니다."),
    INVALID_JWT(605, "CHAT-002", "Jwt 토큰이 유효하지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    ChatErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
