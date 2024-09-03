package org.sparta.newsfeed.friend.dto;

import java.time.LocalDateTime;

public class FriendResponseDto {

    private String friendName;
    private LocalDateTime createAt;

    public FriendResponseDto(String friendName,LocalDateTime createAt){
        this.friendName = friendName;
        this.createAt = createAt;
    }



}
