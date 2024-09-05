package org.sparta.newsfeed.common.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class BadRequestException extends RuntimeException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
