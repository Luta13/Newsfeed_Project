package org.sparta.newsfeed.board.dto;

import lombok.Getter;

@Getter
public class BoardUpdateRequestDto {
    private String title;
    private String content;
}