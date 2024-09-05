package org.sparta.newsfeed.common.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
