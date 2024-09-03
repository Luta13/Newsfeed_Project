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
    @JoinColumn(name = "baseId", nullable = false)
    private User baseEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId", nullable = false)
    private User friendEmail;

    @Column(nullable = false)
    private boolean applyYn;

}
