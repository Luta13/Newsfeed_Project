package org.sparta.newsfeed.comment.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;

public class NoSuchElementException extends NotFoundException {

    public NoSuchElementException(ErrorCode errorCode) { super(errorCode); }
}
