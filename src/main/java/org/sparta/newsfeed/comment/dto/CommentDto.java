package org.sparta.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private String commentContent;

    private Long userId;

    private String memberName;

    private Long boardId;

    private String boardTitle;

    private String boardContent;

    private LocalDateTime createDt;

    private LocalDateTime modifyDt;

}