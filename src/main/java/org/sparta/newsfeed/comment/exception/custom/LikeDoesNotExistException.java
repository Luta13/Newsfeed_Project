package org.sparta.newsfeed.comment.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class LikeDoesNotExistException extends RuntimeException {

    public LikeDoesNotExistException(ErrorCode errorCode) { super(errorCode.getMessage()); }
}
