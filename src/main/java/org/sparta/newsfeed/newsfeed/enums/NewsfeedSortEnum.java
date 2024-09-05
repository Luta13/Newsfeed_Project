package org.sparta.newsfeed.newsfeed.enums;

import lombok.Getter;

@Getter
public enum NewsfeedSortEnum {
    CREATEDAT("createdAt"),
    MODIFIEDAT("modifiedAt"),
    LIKECNT("likeCnt");

    private final String sort;

    NewsfeedSortEnum(String sort) {
        this.sort = sort;
    }
}
