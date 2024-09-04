package org.sparta.newsfeed.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private String commentContent;
    private int commentLikeCount;
    private String memberName;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime createDt;
    private LocalDateTime modifyDt;


}
