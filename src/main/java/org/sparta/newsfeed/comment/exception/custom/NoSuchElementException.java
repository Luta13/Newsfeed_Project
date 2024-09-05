package org.sparta.newsfeed.comment.exception.custom;

import org.sparta.newsfeed.common.exception.code.ErrorCode;

public class NoSuchElementException extends RuntimeException {

    public NoSuchElementException(ErrorCode errorCode) { super(errorCode.getMessage()); }
}
