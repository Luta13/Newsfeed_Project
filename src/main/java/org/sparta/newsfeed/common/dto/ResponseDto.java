package org.sparta.newsfeed.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    private int status;
    private T body;
    private String msg;

}