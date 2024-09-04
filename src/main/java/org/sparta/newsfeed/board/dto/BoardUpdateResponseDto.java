package org.sparta.newsfeed.board.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardUpdateResponseDto {
    private final Long boardId;
    private final String title;
    private final String content;
    private final String username;
    private final LocalDateTime modifyDt;

    public BoardUpdateResponseDto(
            Long boardId,
            String title,
            String content,
            String username,
            LocalDateTime modifyDt) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.username = username;
        this.modifyDt = modifyDt;
    }
}
