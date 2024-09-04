package org.sparta.newsfeed.friend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FriendResponseDto {

    private final String friendName;
    private final LocalDateTime createAt;

    public FriendResponseDto(String friendName,LocalDateTime createAt){
        this.friendName = friendName;
        this.createAt = createAt;
    }



}
