package org.sparta.newsfeed.friend.repository;

import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByBaseIdAndFriendId(User baseId, User friendId);
    List<Friend> findByFriendIdAndApplyYnFalse(User friendId);
    List<Friend> findByFriendIdAndApplyYnTrue(User friendId);
    void deleteByBaseIdOrFriendId(User baseId, User friendId);

}
