package com.climbing.api.chat.exception;

import com.climbing.global.exception.BaseException;
import com.climbing.global.exception.BaseExceptionType;

public class ChatRoomException extends BaseException {
    private final BaseExceptionType exceptionType;

    public ChatRoomException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
