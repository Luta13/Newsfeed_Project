package org.sparta.newsfeed.comment.exception;

import org.sparta.newsfeed.comment.exception.custom.AlreadyLikedCommentException;
import org.sparta.newsfeed.comment.exception.custom.CommentAuthorizationException;
import org.sparta.newsfeed.comment.exception.custom.LikeDoesNotExistException;
import org.sparta.newsfeed.comment.exception.custom.NoSuchElementException;
import org.sparta.newsfeed.common.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentExceptionHandler {

    @ExceptionHandler(CommentAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleCommentAuthorizationException(CommentAuthorizationException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyLikedCommentException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyLikedCommentException(AlreadyLikedCommentException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LikeDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleLikeDoesNotExistException(LikeDoesNotExistException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
