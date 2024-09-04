package org.sparta.newsfeed.newsfeed.dto;

import java.time.LocalDateTime;

public interface NewsfeedResponseDto {
    String getTitle();
    String getContent();
    String getUserName();
    Long getCommentCnt();
    Long getLikeCnt();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();
}
