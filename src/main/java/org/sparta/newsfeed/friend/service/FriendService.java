package org.sparta.newsfeed.friend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;
import org.sparta.newsfeed.common.exception.custom.ConflictException;
import org.sparta.newsfeed.common.exception.custom.ForbiddenException;
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

        if (user.equals(requestUser)){
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }
        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(user,requestUser).orElse(null); // Baseid와 FriendId가 들어온 값과 똑같은 Friend객체 가져옴 즉 친구요청을 했거나 친구인 상태인지 검증하기 위함
        if(isAlreadyFriends != null) {
            if (isAlreadyFriends.isApplyYn()) {
                throw new ConflictException(ErrorCode.ALREADY_FRIEND);
            } else if (isAlreadyFriends.isApplyYn() == false) {
                throw new ConflictException(ErrorCode.ALREADY_FRIEND_REQUESTED);
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

        if (user.equals(requestUser)) {
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }
        Friend friend = friendRepository.findByBaseIdAndFriendId(requestUser, user)
                .orElseThrow(() -> new BadRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        if(/*거짓*/!friend.isApplyYn())
        {
            friendRepository.delete(friend);
        }
        else
        {
            throw new ConflictException(ErrorCode.ALREADY_FRIEND);
        }


    }

    public void deleteFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        /*예외 처리*/

        Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                (() -> new ForbiddenException(ErrorCode.NOT_A_FRIEND));
        Friend reverseFriend = friendRepository.findByBaseIdAndFriendId(requestUser, user).orElseThrow
                (() -> new ForbiddenException(ErrorCode.NOT_A_FRIEND));


        if (friend.isApplyYn()) {
            friendRepository.delete(friend);
            friendRepository.delete(reverseFriend);
        }
        else {
            throw new ForbiddenException((ErrorCode.NOT_A_FRIEND));
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
            throw new ForbiddenException(ErrorCode.NOT_A_FRIEND);
        }



        return requestFriends.stream().map(friend -> new FriendResponseDto(
                friend.getBaseId().getName(), friend.getBaseId().getCreatedAt())).toList();
    }

    public void acceptFriends(AuthUser authUser, FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());


        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(requestUser,user).orElse(null);
        if(user.equals(requestUser)) {
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }
        if(isAlreadyFriends.isApplyYn())
        {
            throw new ConflictException(ErrorCode.ALREADY_FRIEND);
        }
        else {
            Friend friend = friendRepository.findByBaseIdAndFriendId(user, requestUser).orElseThrow
                    (() -> new BadRequestException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
            friend.setApplyYn(true);
            friendRepository.save(friend);

            Friend reverseFriend = new Friend(requestUser, user, true);
            friendRepository.save(reverseFriend);
        }

    }

    public void rejectFriends(AuthUser authUser,FriendDto friendDto) {
        User user = userService.findByEmail(authUser.getEmail());
        User requestUser = userService.findByEmail(friendDto.getRequestEmail());

        if(user.equals(requestUser)) {
            throw new BadRequestException(ErrorCode.SELF_FRIEND_REQUEST_NOT_ALLOWED);
        }
        Friend isAlreadyFriends = friendRepository.findByBaseIdAndFriendId(requestUser,user).orElse(null);
        if(/*거짓*/isAlreadyFriends.isApplyYn()){
            friendRepository.delete(isAlreadyFriends);
        }
        else if(isAlreadyFriends.isApplyYn())
        {
            throw new ConflictException(ErrorCode.ALREADY_FRIEND);
        }

    }




}
