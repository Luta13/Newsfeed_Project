package org.sparta.newsfeed.friend.repository;

import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByBaseIdAndFriendId(User baseId, User friendId);
//    boolean existsFriendByFriendIdAndBaseId(User baseId, User friendId);


    void deleteByBaseIdOrFriendId(User baseId, User friendId);
}
