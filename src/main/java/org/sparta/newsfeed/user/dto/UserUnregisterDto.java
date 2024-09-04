package org.sparta.newsfeed.user.dto;

import lombok.Getter;

@Getter
public class UserUnregisterDto {
    private String email;
    private String message;

    public UserUnregisterDto(String email, String message) {
        this.email = email;
        this.message = message;
    }
}
