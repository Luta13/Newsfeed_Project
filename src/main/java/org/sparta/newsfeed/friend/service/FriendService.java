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

    /*유저가 친구 요청을 하는 메서드*/
    public void requestFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());


        /*예외처리*/
        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        if (user.equals(requestUser)){
            throw new IllegalArgumentException("자신에게 친구 요청을 할 수 없습니다.");
        }
        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElse(null); // Baseid와 FriendId가 들어온 값과 똑같은 Friend객체 가져옴 즉 친구요청을 했거나 친구인 상태인지 검증하기 위함
        if(isAlreadyFriends != null) {
            if (isAlreadyFriends.isApplyYn()) {
                throw new IllegalArgumentException("이미 친구입니다.");
            } else if (isAlreadyFriends.isApplyYn() == false) {
                throw new IllegalArgumentException("이미 친구 요청을 하였습니다.");
            }
        }
        else {
            Friend friend = new Friend(requestUser,user, false); // 새로운 Friend객체 생성, applyYn = fals은 요청 대기상태
            friendRepository.save(friend);
        }





    }

    /*유저가 친구 요청을 반려하는 메서드*/
    public void cancelRequestFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        /*예외 처리*/
        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        if (user.equals(requestUser)) {
            throw new IllegalArgumentException("자신에게 친구 요청을 할 수 없습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(requestUser, user).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        if(/*거짓*/!friend.isApplyYn())
        {
            friendRepository.delete(friend);
        }
        else
        {
            throw new RuntimeException("이미 친구입니다.");
        }


    }

    public void deleteFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        /*예외 처리*/
        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구가 아닙니다."));
        Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(requestUser, user).orElseThrow
                (() -> new RuntimeException("친구가 아닙니다."));


        if (friend.isApplyYn()) {
            friendRepository.delete(friend);
            friendRepository.delete(reverseFriend);
        }
        else {
            throw new RuntimeException("두 유저는 친구가 아닙니다.");
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

    /*유저가 친구 요청을 보낸 리스트를 갖고옴*/
    public List<FriendResponseDto> getRequestFriends(AuthUser authUser) {
        List<Friend> requestFriends;
        User user = userService.findByEmail(authUser.getEmail());
        requestFriends = friendRepository.findByFriendIdAndApplyYnFalse(user);

        if(requestFriends.isEmpty()) {
            throw new RuntimeException("신청한 친구요청이 없습니다.");
        }



        return requestFriends.stream().map(friend -> new FriendResponseDto(
                friend.getBaseId().getName(), friend.getBaseId().getCreatedAt())).toList();
    }

    public void acceptFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElse(null);
        if(user.equals(requestUser)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        if(isAlreadyFriends.isApplyYn())
        {
            throw new RuntimeException("이미 친구입니다.");
        }

        Friend friend = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        friend.setApplyYn(true);
        friendRepository.save(friend);

        Friend reverseFriend = new Friend(requestUser, user, true);
        friendRepository.save(reverseFriend);


    }

    public void rejectFriends(AuthUser authUser,FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if (user == null || requestUser == null) {
            throw new IllegalArgumentException("유저를 찾지 못했습니다.");
        }
        if(user.equals(requestUser)) {
            throw new RuntimeException("권한이 없습니다.");
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new RuntimeException("친구요청을 찾지 못했습니다."));
        if(/*거짓*/friend.isApplyYn()){
            friendRepository.delete(friend);
        }
        else if(friend.isApplyYn())
        {
            throw new RuntimeException("이미 친구입니다.");
        }

    }




}
