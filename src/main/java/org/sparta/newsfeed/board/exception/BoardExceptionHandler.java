package org.sparta.newsfeed.board.exception;

import org.sparta.newsfeed.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class BoardExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 필드와 오류 메시지를 담을 Map 생성
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검증 오류 처리
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // 필드별로 여러 유효성 검사를 거치면 첫 번째 오류 메시지만 출력
            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }

        // 에러 메시지를 ResponseDto로 감싸서 반환
        return new ResponseEntity<>(new ResponseDto<>(400, errors, "유효성 검사에 실패했습니다."), HttpStatus.BAD_REQUEST);
    }

}
