package org.sparta.newsfeed.newsfeed.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedRequestDto;
import org.sparta.newsfeed.newsfeed.dto.NewsfeedResponseDto;
import org.sparta.newsfeed.newsfeed.service.NewsfeedService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newsfeed")
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    @GetMapping
    public ResponseEntity<ResponseDto<Page<NewsfeedResponseDto>>> getNewsfeed(@Auth AuthUser authUser,
                                                                      @ModelAttribute NewsfeedRequestDto newsfeedRequestDto) {
        return ResponseEntity.ok(new ResponseDto<>(200 , newsfeedService.getNewsfeed(authUser.getUserId() , newsfeedRequestDto) , "뉴스피드 게시물 조회했습니다."));
    }
}
