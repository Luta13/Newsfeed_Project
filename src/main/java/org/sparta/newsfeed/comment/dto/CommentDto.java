package org.sparta.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private String content;

    private Long userId;

    private Long boardId;

}
