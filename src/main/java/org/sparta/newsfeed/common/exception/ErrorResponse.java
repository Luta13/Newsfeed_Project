package org.sparta.newsfeed.common.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;

    private final int status;

    private final String msg;

    public ErrorResponse(int status, String msg) {

        this.timestamp = LocalDateTime.now();

        this.status = status;

        this.msg = msg;

    }
}
