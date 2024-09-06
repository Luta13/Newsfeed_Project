package org.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRegisterDto {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "유효한 이메일 형식이 아닙니다. 예: email@example.com"
    )
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
            message = "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{1,10}$", message = "이름은 한글, 영문 대소문자로 1~10자 이내로 입력해주세요.")
    private String name;
}
