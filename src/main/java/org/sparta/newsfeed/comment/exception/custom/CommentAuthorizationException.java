package org.sparta.newsfeed.comment.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class CommentAuthorizationException extends RuntimeException {

    public CommentAuthorizationException(ErrorCode errorCode) { super(errorCode.getMessage()); }
}
