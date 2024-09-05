package org.sparta.newsfeed.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardCreateRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
