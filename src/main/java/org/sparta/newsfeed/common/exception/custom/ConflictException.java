package org.sparta.newsfeed.common.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class ConflictException extends RuntimeException {

    public ConflictException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
