package org.sparta.newsfeed.newsfeed.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.newsfeed.friend.entity.Friend;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedRequestDto;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedResponseDto;
import org.sparta.newsfeed.newsfeed.repository.NewsfeedRepository;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NewsfeedService {

    private final NewsfeedRepository newsfeedRepository;
    private final UserService userService;

    // http://localhost:9090/newsfeed?page=1&sort=LIKECNT&startDt=&endDt= 와 같이
    // 기간을 지정하여 조회 하는게 아니라면 모든 기간을 조회하도록 수정한 코드입니다.
    // 또한 로그인 후 뉴스피드페이지로 간다면 천제 기간 좋아요순으로 자동으로 가져오도록 수정했습니다. ( 이부분은 프론트에서 처리 )
    public Page<NewsfeedResponseDto> getNewsfeed(Long userId, NewsfeedRequestDto newsfeedRequestDto) {
        User user = userService.findById(userId);

        // 친구 목록 가져와요
        List<Friend> users = newsfeedRepository.findByBaseIdAndApplyYnTrue(user);
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);
        for (Friend friend : users) {
            userIds.add(friend.getFriendId().getUserId());
        }

        // startDt와 endDt가 비어 있으면 전체 기간 조회
        String startDt = newsfeedRequestDto.getStartDt();
        String endDt = newsfeedRequestDto.getEndDt();

        // 기본 기간 설정 (전체 기간으로 조회하기 위한 과거의 시작일)
        if (startDt == null || startDt.isEmpty()) {
            startDt = "1900-01-01"; // 과거의 날짜로 설정하여 전체 조회
        }
        if (endDt == null || endDt.isEmpty()) {
            endDt = LocalDate.now().toString(); // 현재 날짜로 설정
        }

        // 페이지 요청 및 정렬
        PageRequest pageRequest = PageRequest.of(newsfeedRequestDto.getPage() - 1, 10, Sort.by(newsfeedRequestDto.getSort().getSort()).descending());

        // 리포지토리에서 조회
        return newsfeedRepository.findByNewsfeed(userIds, startDt, endDt, pageRequest);
    }

}
