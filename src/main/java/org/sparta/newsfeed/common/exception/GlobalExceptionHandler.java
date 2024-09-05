package org.sparta.newsfeed.common.exception;

import org.sparta.newsfeed.common.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto<String>> BadRequestHandle(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(HttpStatus.BAD_REQUEST.value() , "" , e.getMessage()));
    }

    @ExceptionHandler(value = ConflictException.class)
    public ResponseEntity<ResponseDto<String>> ConflictHandle(ConflictException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto<>(HttpStatus.CONFLICT.value() , "" , e.getMessage()));
    }

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ResponseDto<String>> ForbiddenHandle(ForbiddenException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto<>(HttpStatus.FORBIDDEN.value() , "" , e.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ResponseDto<String>> NotFoundHandle(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>(HttpStatus.NOT_FOUND.value() , "" , e.getMessage()));
    }
}
