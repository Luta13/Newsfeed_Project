package org.sparta.newsfeed.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.common.entity.Timestamped;
import org.sparta.newsfeed.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
public class Board extends Timestamped {

    @Id
    private Long boardId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardLike> boardLikeList = new ArrayList<>();
}
