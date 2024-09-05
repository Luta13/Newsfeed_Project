package org.sparta.newsfeed.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;
import org.sparta.newsfeed.common.exception.custom.ConflictException;
import org.sparta.newsfeed.common.exception.custom.ForbiddenException;
import org.sparta.newsfeed.common.exception.custom.NotFoundException;
import org.sparta.newsfeed.friend.dto.FriendDto;
import org.sparta.newsfeed.friend.dto.FriendResponseDto;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        // 자기 자신한테 친추는 불가능
        if (user.getUserId().equals(requestUser.getUserId())){
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }

        Optional<Friend> otherIsAlReadyFriends = friendRepository.findByBaseIdAndFriendId(requestUser , user);
        // 만약 친구 요청온 친구한테 내쪽에서 요청을 보낼 경우?
        if (otherIsAlReadyFriends.isPresent()) {
            Friend friend1 = otherIsAlReadyFriends.get();
            friend1.updateApplyYn(true);
            Friend friend2 = new Friend(user , requestUser, true);

            friendRepository.save(friend1);
            friendRepository.save(friend2);
            return;
        }

        // Baseid와 FriendId가 들어온 값과 똑같은 Friend객체 가져옴 즉 친구요청을 했거나 친구인 상태인지 검증하기 위함
        Optional<Friend> isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(user , requestUser);
        if(isAlreadyFriends.isPresent()) {
            if (isAlreadyFriends.get().isApplyYn()) {
                throw new ConflictException(ErrorCode.ALREADY_FRIEND);
            } else {
                throw new ConflictException(ErrorCode.ALREADY_FRIEND_REQUESTED);
            }
        }
        else {
            Friend friend = new Friend(user , requestUser, false); // 새로운 Friend객체 생성, applyYn = fals은 요청 대기상태
            friendRepository.save(friend);
        }
    }

    /*유저가 친구 요청을 반려하는 메서드*/
    public void cancelRequestFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        Optional<Friend> isAlreadyFriends = friendRepository.findByBaseIdAndFriendIdAndApplyYnFalse(user , requestUser);
        if (isAlreadyFriends.isPresent()) {
            friendRepository.delete(isAlreadyFriends.get());
        } else {
            // 요청이 없는 친구 throw 처리
            throw new BadRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    public void deleteFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        Optional<Friend> friend = friendRepository.findByBaseIdAndFriendIdAndApplyYnTrue(user, requestUser);
        Optional<Friend> reverseFriend = friendRepository.findByBaseIdAndFriendIdAndApplyYnTrue(requestUser, user);

        if (friend.isPresent() && reverseFriend.isPresent()) {
            friendRepository.delete(friend.get());
            friendRepository.delete(reverseFriend.get());
        } else {
            throw new ForbiddenException(ErrorCode.NOT_A_FRIEND);
        }
    }

    public List<FriendResponseDto> getFriends(AuthUser authUser) {
        User user = userService.findByEmail(authUser.getEmail());
        List<Friend> friends = friendRepository.findByBaseIdAndApplyYnTrue(user);

        return friends.stream().map(friend -> new FriendResponseDto(
                friend.getBaseId().getName(), friend.getBaseId().getCreatedAt())).toList();
    }

    public List<FriendResponseDto> getRequestFriends(AuthUser authUser) {
        User user = userService.findByEmail(authUser.getEmail());
        List<Friend> requestFriends = friendRepository.findByFriendIdAndApplyYnFalse(user);

        if(requestFriends.isEmpty()) {
            throw new NotFoundException(ErrorCode.FRIEND_NOT_FOUND);
        }

        return requestFriends.stream().map(friend -> new FriendResponseDto(
                friend.getBaseId().getName(), friend.getBaseId().getCreatedAt())).toList();
    }

    public void acceptFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if(user.getUserId().equals(requestUser.getUserId())) {
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_APPROVAL_NOT_ALLOWED);
        }

        Optional<Friend> me = friendRepository.findByBaseIdAndFriendIdAndApplyYnFalse(requestUser,user);

        if (me.isPresent()) {
            Friend friend1 = me.get();
            friend1.updateApplyYn(true);
            Friend friend2 = new Friend(user , requestUser , true);
            friendRepository.save(friend1);
            friendRepository.save(friend2);
        } else {
            throw new BadRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }
    }

    public void rejectFriends(AuthUser authUser,FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if(user.getUserId().equals(requestUser.getUserId())) {
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }

        Optional<Friend> isAlreadyFriends = friendRepository.findByBaseIdAndFriendIdAndApplyYnFalse(requestUser,user);

        if (isAlreadyFriends.isPresent()) {
            friendRepository.delete(isAlreadyFriends.get());
        } else {
            // 거절할 요청이 없음
            throw new BadRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
        }
    }
}
