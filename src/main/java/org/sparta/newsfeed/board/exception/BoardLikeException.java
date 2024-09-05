package org.sparta.newsfeed.board.exception;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;

public class BoardLikeException extends BadRequestException {
    public BoardLikeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
