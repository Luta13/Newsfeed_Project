package org.sparta.newsfeed.user.dto;

import lombok.Getter;

@Getter
public class UserPasswordUpdateDto {
    private String email;
    private String originalPassword;
    private String changePassword;

    public UserPasswordUpdateDto(String email, String originalPassword, String changePassword) {
        this.email = email;
        this.originalPassword = originalPassword;
        this.changePassword = changePassword;
    }
}

