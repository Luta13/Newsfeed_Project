package org.sparta.newsfeed.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.dto.FriendResponseDto;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    public void requestFriends(AuthUser authUser, String requestEmail) {
        User user = userService.findUserByEmail(authUser.getEmail());
        User requestUser = userService.findUserByEmail(requestEmail);

        log.info("userEmail",user.getEmail());
        log.info("requestEmail",requestUser.getEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        if (user.equals(requestUser)){
            throw new IllegalArgumentException("자신에게 친구 요청을 할 수 없습니다.");
        }



        Friend friend = new Friend(user, requestUser, false);
        friendRepository.save(friend);
    }

    public void cancelRequestFriends(AuthUser authUser, String requestEmail) {
        User user = userService.findUserByEmail(authUser.getEmail());
        User requestUser = userService.findUserByEmail(requestEmail);

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));

        friendRepository.delete(friend);
    }

    public void deleteFriends(AuthUser authUser, String requestEmail) {
        User user = userService.findUserByEmail(authUser.getEmail());
        User requestUser = userService.findUserByEmail(requestEmail);

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구가 아닙니다."));

        if (friend.isApplyYn()) {
            friendRepository.delete(friend);
            Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                    (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
            friendRepository.delete(reverseFriend);
        }

    }

    public List<FriendResponseDto> getFriends(AuthUser authUser) {
        List<Friend> baseFriends = new ArrayList<>();
        for (Friend friend : friendRepository.findAll()) {
            if (authUser.getUserId().equals(friend.getBaseId().getUserId())) {
                if (friend.isApplyYn()) {
                    baseFriends.add(friend);
                }
            }
        }
        return baseFriends.stream().map(friend -> new FriendResponseDto(
                friend.getFriendId().getName(), friend.getFriendId().getCreatedAt())).
                collect(Collectors.toList());
    }

    public List<FriendResponseDto> getRequestFriends(AuthUser authUser) {
        List<Friend> requestFriends = new ArrayList<>();

        for (Friend friend : friendRepository.findAll()) {
            if (authUser.getUserId().equals(friend.getBaseId().getUserId())) {
                if (/*거짓*/!friend.isApplyYn())
                    requestFriends.add(friend);
            }
        }

        return requestFriends.stream().map(friend -> new FriendResponseDto(
                friend.getFriendId().getName(), friend.getFriendId().getCreatedAt())).
                collect(Collectors.toList());
    }

    public void acceptFriends(AuthUser authUser, String requestEmail) {
        User user = userService.findUserByEmail(authUser.getEmail());
        User requestUser = userService.findUserByEmail(requestEmail);

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));

        friend.setApplyYn(true);

        Friend reverseFriend = new Friend(requestUser, user, true);

        friendRepository.save(friend);
        friendRepository.save(reverseFriend);

    }

    public void rejectFriends(AuthUser authUser, String requestEmail) {
        User user = userService.findUserByEmail(authUser.getEmail());
        User requestUser = userService.findUserByEmail(requestEmail);

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }

        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));

        friendRepository.delete(friend);
    }


}
