package org.sparta.newsfeed.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]{1,50}$", message = "제목은 한글, 영문, 숫자로 1~50자 이내로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9]{1,3000}$", message = "내용은 한글, 영문, 숫자로 1~3000자 이내로 입력해주세요.")
    private String content;

}
