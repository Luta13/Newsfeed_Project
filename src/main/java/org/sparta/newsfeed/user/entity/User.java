package org.sparta.newsfeed.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sparta.newsfeed.board.entity.Board;
import org.sparta.newsfeed.board.entity.BoardLike;
import org.sparta.newsfeed.comment.entity.Comment;
import org.sparta.newsfeed.comment.entity.CommentLike;
import org.sparta.newsfeed.common.entity.Timestamped;
import org.sparta.newsfeed.friend.entity.Friend;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatusEnum status;

    @OneToMany(mappedBy = "user")
    private List<Board> scheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommentLike> commentLikeList = new ArrayList<>();

    public void updateToken(String token) {
        this.refreshToken = token;
    }

    public User(String email, String password, String name, String refreshToken, UserStatusEnum status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.refreshToken = refreshToken;
        this.status = status;
    }

    // 비밀번호 변경
    public void changePassword(String changePassword) {
        this.password = changePassword;
    }

    // 이름 변경
    public void updateName(String updateName) {
        this.name = updateName;
    }

    // 이메일 변경
    public void updateEmail(String updateEmail) {
        this.email = updateEmail;
    }
}
