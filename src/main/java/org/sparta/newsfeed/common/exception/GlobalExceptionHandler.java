package org.sparta.newsfeed.common.exception;

import org.sparta.newsfeed.common.exception.custom.BadRequestException;
import org.sparta.newsfeed.common.exception.custom.ConflictException;
import org.sparta.newsfeed.common.exception.custom.ForbiddenException;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> BadRequestHandle(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value() , e.getMessage()));
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ErrorResponse> ConflictHandle(ConflictException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value() , e.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ErrorResponse> ForbiddenHandle(ForbiddenException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN.value() , e.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> NotFoundHandle(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value() , e.getMessage()));
    }
}
