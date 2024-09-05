package org.sparta.newsfeed.board.dto;

import java.time.LocalDateTime;

public interface BoardGetResponseDto {
    String getTitle();
    String getContent();
    String getUserName();
    Long getCommentCnt();
    Long getLikeCnt();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();
}
