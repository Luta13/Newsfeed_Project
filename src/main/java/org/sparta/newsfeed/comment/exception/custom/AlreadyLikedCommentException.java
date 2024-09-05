package org.sparta.newsfeed.comment.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class AlreadyLikedCommentException extends RuntimeException {

    public AlreadyLikedCommentException(ErrorCode errorCode) { super(errorCode.getMessage()); }
}
