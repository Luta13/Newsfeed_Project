package org.sparta.newsfeed.common.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
