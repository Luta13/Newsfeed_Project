package org.sparta.newsfeed.friend.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
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

        return ResponseEntity.ok(friendService.requestFriends(authUser,requestEmail);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelRequestFriends(@Auth AuthUser authUser, String requestEmail)
    {
        return ResponseEntity.ok(friendService.cancelRequestFriends(authUser,requestEmail);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> deleteFriends(@Auth AuthUser authUser, String requestEmail)
    {
        return ResponseEntity.ok(friendService.deleteFriends(authUser, requestEmail));
    }

    @GetMapping("/requests")
    public void getRequestFriends(@Auth AuthUser authUser)
    {
        return friendService.getRequestFriends(authUser);
    }
    @GetMapping
    public void getFriends(@Auth AuthUser authUser)
    {
        return friendService.getFriends(authUser);
    }

    @PostMapping("/accept")
    public void acceptFriends(@Auth AuthUser authUser, String requestEmail)
    {
        return friendService.acceptFriends(authUser, requestEmail);
    }
    @PostMapping("/reject")
    public void rejectFriends(@Auth AuthUser authUser, String requestEmail)
    {
        return friendService.rejectFriends(authUser, requestEmail);
    }





}
