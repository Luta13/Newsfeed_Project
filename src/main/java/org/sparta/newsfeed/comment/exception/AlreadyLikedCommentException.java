package org.sparta.newsfeed.comment.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;

public class AlreadyLikedCommentException extends BadRequestException {

    public AlreadyLikedCommentException(ErrorCode errorCode) { super(errorCode); }
}
