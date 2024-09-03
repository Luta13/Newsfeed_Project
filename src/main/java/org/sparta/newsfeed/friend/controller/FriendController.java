package org.sparta.newsfeed.friend.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.dto.FriendResponseDto;
import org.sparta.newsfeed.friend.entity.Friend;
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
    public ResponseEntity<String> requestFriends(@Auth AuthUser authUser, String requestEmail){

        friendService.requestFriends(authUser,requestEmail);
        return ResponseEntity.status(200).body("친구추가 요청이 완료되었습니다.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelRequestFriends(@Auth AuthUser authUser, String requestEmail)
    {
        friendService.cancelRequestFriends(authUser,requestEmail);
        return ResponseEntity.status(204).body("친구추가 요청이 반려되엇습니다");
    }

    @PostMapping("/remove")
    public ResponseEntity<String> deleteFriends(@Auth AuthUser authUser, String requestEmail)
    {
        friendService.deleteFriends(authUser, requestEmail);
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
        return ResponseEntity.status(200).body(friendService.getFriends(authUser));
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriends(@Auth AuthUser authUser, String requestEmail)
    {
        friendService.acceptFriends(authUser,requestEmail);
        return ResponseEntity.status(200).body("친구 요청 승인이 완료되었습니다.");
    }
    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriends(@Auth AuthUser authUser, String requestEmail)
    {
        friendService.rejectFriends(authUser,requestEmail);
        return ResponseEntity.status(200).body("친구 요청이 거절되었습니다.");
    }





}
