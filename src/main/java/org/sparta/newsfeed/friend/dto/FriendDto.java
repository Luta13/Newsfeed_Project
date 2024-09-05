package org.sparta.newsfeed.friend.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FriendDto {
    @NotBlank
    private String requestEmail;
}
