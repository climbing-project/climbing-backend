package com.climbing.api.chat.exception;

import com.climbing.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ChatRoomExceptionType implements BaseExceptionType {
    NOT_FOUND_CHATROOM(603, HttpStatus.NOT_FOUND, "일치하는 채팅방이 존재하지 않습니다.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ChatRoomExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
