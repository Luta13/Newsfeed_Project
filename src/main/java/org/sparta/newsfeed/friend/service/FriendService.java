package org.sparta.newsfeed.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.dto.FriendDto;
import org.sparta.newsfeed.friend.dto.FriendResponseDto;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;

    public void requestFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());



        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        if (user.equals(requestUser)){
            throw new IllegalArgumentException("자신에게 친구 요청을 할 수 없습니다.");
        }
        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElse(null);
        if(isAlreadyFriends != null) {
            if (isAlreadyFriends.isApplyYn()) {
                throw new IllegalArgumentException("이미 친구입니다.");
            } else if (isAlreadyFriends.isApplyYn() == false) {
                throw new IllegalArgumentException("이미 친구 요청을 하였습니다.");
            }
        }




        Friend friend = new Friend(user, requestUser, false);
        Friend reverseFriend = new Friend(requestUser, user, false);
        friendRepository.save(friend);
        friendRepository.save(reverseFriend);

    }

    public void cancelRequestFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }

        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        Friend reversefriend = friendRepository.findByBaseIdAndFriendId(requestUser, user).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));

        friendRepository.delete(friend);
        friendRepository.delete(reversefriend);
    }

    public void deleteFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구가 아닙니다.213"));

        if (friend.isApplyYn()) {
            friendRepository.delete(friend);
            Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(requestUser, user).orElseThrow
                    (() -> new RuntimeException("친구가 아닙니다."));
            friendRepository.delete(reverseFriend);
        }

    }
    public void deleteFriends(User user)
    {
         friendRepository.deleteByBaseIdOrFriendId(user,user);
    }

    public List<FriendResponseDto> getFriends(AuthUser authUser) {
        List<Friend> baseFriends;
        User user = userService.findByEmail(authUser.getEmail());
        baseFriends = friendRepository.findByFriendIdAndApplyYnTrue(user);




        return baseFriends.stream().map(friend1 -> new FriendResponseDto(
                friend1.getBaseId().getName(), friend1.getBaseId().getCreatedAt())).toList();
    }

    public List<FriendResponseDto> getRequestFriends(AuthUser authUser) {
        List<Friend> requestFriends;
        User user = userService.findByEmail(authUser.getEmail());
        requestFriends = friendRepository.findByFriendIdAndApplyYnFalse(user);


        return requestFriends.stream().map(friend -> new FriendResponseDto(
                friend.getBaseId().getName(), friend.getBaseId().getCreatedAt())).toList();
    }

    public void acceptFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }

        Friend friend = friendRepository.findByBaseIdAndFriendId(requestUser,user).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        friend.setApplyYn(true);
        reverseFriend.setApplyYn(true);

        friendRepository.save(friend);
        friendRepository.save(reverseFriend);

    }

    public void rejectFriends(AuthUser authUser,FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }

        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));

        friendRepository.delete(friend);
        friendRepository.save(reverseFriend);
    }




}
