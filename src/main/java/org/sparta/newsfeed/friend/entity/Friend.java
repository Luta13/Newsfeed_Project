package org.sparta.newsfeed.friend.entity;

import jakarta.persistence.*;
import org.sparta.newsfeed.user.entity.User;

@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "base_email", nullable = false)
//    private User baseEmail;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "friend_email", nullable = false)
//    private User friendEmail;
//
//    @Enumerated(value = EnumType.STRING)
//    private FriendApplyEnum applyYn;

}
