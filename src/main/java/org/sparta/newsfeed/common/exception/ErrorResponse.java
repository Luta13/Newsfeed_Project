package org.sparta.newsfeed.common.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;

    private final int status;

    private final String message;

    public ErrorResponse(int status, String message) {

        this.timestamp = LocalDateTime.now();

        this.status = status;

        this.message = message;

    }
}
