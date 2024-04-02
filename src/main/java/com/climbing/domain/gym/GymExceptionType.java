package com.climbing.domain.gym;

import com.climbing.global.exception.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum GymExceptionType implements BaseExceptionType {

    GYM_NOT_FOUND(700, HttpStatus.NOT_FOUND, "일치하는 암장이 존재하지 않습니다."),
    SORT_TYPE_NOT_FOUND(701, HttpStatus.NOT_FOUND, "잘못된 정렬 방식입니다.");

    private final int errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    GymExceptionType(int errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }


    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
