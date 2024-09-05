package org.sparta.newsfeed.user.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;

public class UserInvalidException extends BadRequestException {
    public UserInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
