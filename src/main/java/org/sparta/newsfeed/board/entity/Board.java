package org.sparta.newsfeed.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Board {

    @Id
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany
    private List<BoardLike> boardLikeList = new ArrayList<>();
}
