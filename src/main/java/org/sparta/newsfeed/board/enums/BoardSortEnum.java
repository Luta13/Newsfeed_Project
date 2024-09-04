package org.sparta.newsfeed.board.enums;

import lombok.Getter;

@Getter
public enum BoardSortEnum {
    CREATEDAT("createdAt"),
    MODIFIEDAT("modifiedAt"),
    LIKECNT("likeCnt");

    private final String sort;

    BoardSortEnum(String sort) {
        this.sort = sort;
    }
}
