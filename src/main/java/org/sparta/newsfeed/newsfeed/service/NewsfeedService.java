package org.sparta.newsfeed.newsfeed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedRequestDto;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedResponseDto;
import org.sparta.newsfeed.newsfeed.repository.NewsfeedRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NewsfeedService {

    private final NewsfeedRepository newsfeedRepository;
    private final UserRepository userRepository;

    public Page<NewsfeedResponseDto> getNewsfeed(Long userId , NewsfeedRequestDto newsfeedRequestDto) {
        // TODO : userRepository 가 아니라 userService 에 findById로 변경 예정
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Friend> users = newsfeedRepository.findByBaseIdAndApplyYnTrue(user);
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        for (Friend friend : users) {
            userIds.add(friend.getFriendId().getUserId());
        }

        PageRequest pageRequest = PageRequest.of(newsfeedRequestDto.getPage() - 1 , 10 , Sort.by(newsfeedRequestDto.getSort().getSort()).descending());
        return newsfeedRepository.findByNewsfeed(userIds , newsfeedRequestDto.getStartDt() , newsfeedRequestDto.getEndDt() , pageRequest);
    }
}
