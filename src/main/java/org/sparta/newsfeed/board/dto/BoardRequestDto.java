package org.sparta.newsfeed.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.sparta.newsfeed.board.enums.BoardSortEnum;


@Getter
@Setter
public class BoardRequestDto {
    private int page = 1;
    private BoardSortEnum sort;
    private String startDt;
    private String endDt;
}