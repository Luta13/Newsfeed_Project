package org.sparta.newsfeed.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardGetResponseDto {
    private final String title;
    private final String content;
    private final String username;
    private final int commentCnt;
    private final int likesCnt;
    private final LocalDateTime createAt;
    private final LocalDateTime modifyDt;


    public BoardGetResponseDto(
            String title,
            String content,
            String username,
            int commentCnt,
            int likesCnt,
            LocalDateTime createAt,
            LocalDateTime modifyDt) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.commentCnt = commentCnt;
        this.likesCnt = likesCnt;
        this.createAt = createAt;
        this.modifyDt = modifyDt;
    }
}
