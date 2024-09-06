package org.sparta.newsfeed.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDetailsResponseDto {

    private Long boardId;

    private String title;

    private String content;

    private String name;

    private int bordLikeCount;

}