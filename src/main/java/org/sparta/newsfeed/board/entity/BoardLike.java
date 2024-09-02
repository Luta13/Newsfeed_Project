package org.sparta.newsfeed.board.entity;

import jakarta.persistence.*;
import org.sparta.newsfeed.user.entity.User;


@Table(name ="board_like")
@Entity
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardLikeId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board;




}
