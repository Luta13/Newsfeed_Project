package org.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserProfileUpdateDto {
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String name; // 변경할 이름(선택사항)

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    private final String updateEmail; // 변경할 이메일(선택사항)

    public UserProfileUpdateDto(String email, String name, String updateEmail) {
        this.email = email;
        this.name = name;
        this.updateEmail = updateEmail;
    }
}
