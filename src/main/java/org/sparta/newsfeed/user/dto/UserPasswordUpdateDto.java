package org.sparta.newsfeed.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateDto {
    private String originalPassword;
    private String changePassword;
}

