package org.sparta.newsfeed.comment.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;

public class LikeDoesNotExistException extends NotFoundException {

    public LikeDoesNotExistException(ErrorCode errorCode) { super(errorCode); }
}
