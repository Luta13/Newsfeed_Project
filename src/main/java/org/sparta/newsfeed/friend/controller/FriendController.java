package org.sparta.newsfeed.friend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.dto.FriendDto;
import org.sparta.newsfeed.friend.dto.FriendResponseDto;
import org.sparta.newsfeed.friend.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/friends")
@RestController
public class FriendController {

    private final FriendService friendService;
    @PostMapping("/request")
    public ResponseEntity<String> requestFriends(@Auth AuthUser authUser, @RequestBody FriendDto friendDto){

        friendService.requestFriends(authUser, friendDto);
        return ResponseEntity.status(200).body("친구추가 요청이 완료되었습니다.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelRequestFriends(@Auth AuthUser authUser,@RequestBody FriendDto friendDto)
    {
        friendService.cancelRequestFriends(authUser,friendDto);
        return ResponseEntity.status(200).body("친구추가 요청이 반려되엇습니다");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> deleteFriends(@Auth AuthUser authUser,@RequestBody FriendDto friendDto)
    {
        friendService.deleteFriends(authUser, friendDto);
        return ResponseEntity.status(200).body("친구삭제가 완료되었습니다.");
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendResponseDto>> getRequestFriends(@Auth AuthUser authUser)
    {
        return ResponseEntity.status(200).body(friendService.getRequestFriends(authUser));
    }
    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> getFriends(@Auth AuthUser authUser)
    {
        List<FriendResponseDto> friendReponseDtolist= friendService.getFriends(authUser);
        return ResponseEntity.status(200).body(friendReponseDtolist);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriends(@Auth AuthUser authUser,@RequestBody FriendDto friendDto)
    {
        friendService.acceptFriends(authUser,friendDto);
        return ResponseEntity.status(200).body("친구 요청 승인이 완료되었습니다.");
    }
    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriends(@Auth AuthUser authUser,@RequestBody FriendDto friendDto)
    {
        friendService.rejectFriends(authUser,friendDto);
        return ResponseEntity.status(200).body("친구 요청이 거절되었습니다.");
    }





}
