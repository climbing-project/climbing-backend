package com.climbing.domain.gym;

import com.climbing.global.exception.BaseException;
import com.climbing.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public class GymException extends BaseException {

    private BaseExceptionType exceptionType;

    public GymException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.exceptionType;
    }

}
