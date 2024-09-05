package org.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserProfileUpdateDto {
    @NotBlank
    private String name;
}
