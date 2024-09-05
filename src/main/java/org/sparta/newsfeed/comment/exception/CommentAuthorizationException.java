package org.sparta.newsfeed.comment.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.ForbiddenException;

public class CommentAuthorizationException extends ForbiddenException {
    public CommentAuthorizationException(ErrorCode errorCode) { super(errorCode); }
}
