package org.sparta.newsfeed.friend.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    public String requestFriends(AuthUser authUser, String requestEmail) {
        User user = userRepository.findById(authUser.getUserId()).orElse(null);
        User requestUser = userRepository.findByEmail(requestEmail).orElse(null );
        Friend friend = new Friend(user, requestUser, false);
        friendRepository.save(friend);

    }

    public String cancelRequestFriends(AuthUser authUser, String requestEmail) {
        return null;
    }

    public String deleteFriends(AuthUser authUser, String requestEmail) {
    }

    public void getFriends(AuthUser authUser) {

    }

    public void getRequestFriends(AuthUser authUser) {
    }

    public void acceptFriends(AuthUser authUser, String requestEmail) {
    }

    public void rejectFriends(AuthUser authUser, String requestEmail) {
    }
}
