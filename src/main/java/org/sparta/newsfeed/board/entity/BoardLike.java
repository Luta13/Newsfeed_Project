package org.sparta.newsfeed.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.common.entity.Timestamped;
import org.sparta.newsfeed.user.entity.User;


@Entity
@Getter
@NoArgsConstructor
@Table(name ="board_like")
public class BoardLike extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public BoardLike(User user, Board board) {
        this.user = user;
        this.board = board;
    }

//    public BoardLike(User user, Board board) {
//        this.user = user;
//        this.board = board;
//    }
}
