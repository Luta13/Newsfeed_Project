package org.sparta.newsfeed.friend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sparta.newsfeed.common.entity.Timestamped;
import org.sparta.newsfeed.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "friend")
public class Friend extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseEmail", nullable = false, insertable = false, updatable = false)
    private User baseEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendEmail", nullable = false, insertable = false, updatable = false)
    private User friendEmail;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private FriendApplyEnum applyYn;

}
