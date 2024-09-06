package org.sparta.newsfeed.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.user.dto.*;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    // 다음부터는 DTO에서 유효성 검사를 하면 @Valid 어노테이션 넣어주세요 없으면 사실상 유효성 검사가 안됩니다.
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        // 유효성 검사를 통과한 경우
        userService.registerUser(userRegisterDto);
        return ResponseEntity.ok(new ResponseDto<>(200, null, "회원가입 완료되었습니다."));
    }

    // 회원탈퇴
    @DeleteMapping("/unregister")
    public ResponseEntity<ResponseDto<String>> deleteAccount(@Auth AuthUser authUser, @RequestBody UserUnregisterDto userUnregisterDto) {
        userService.deleteAccount(authUser, userUnregisterDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "회원탈퇴가 완료되었습니다."));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> loginUser(@Valid @RequestBody UserLoginDto userLoginDto , HttpServletResponse response) {
        List<String> tokens =  userService.loginUser(userLoginDto);
        response.addHeader("ACCESS_TOKEN", tokens.get(0));
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "로그인에 성공했습니다."));
    }

    // 자신의 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto<UserProfileDto>> getProfile(@Auth AuthUser authUser) {
        return ResponseEntity.ok(new ResponseDto<>(200 , userService.getProfile(authUser.getUserId()) , "프로필 조회했습니다."));
    }

    // 다른 사용자 프로필 조회
    @GetMapping("/profile/user")
    public ResponseEntity<ResponseDto<UserProfileDto>> getUserProfile(@RequestParam String email) {
        return ResponseEntity.ok(new ResponseDto<>(200 , userService.getUserProfile(email) , "프로필 조회했습니다."));
    }

    // 비밀번호 변경
    @PatchMapping("/change-password")
    public ResponseEntity<ResponseDto<String>> changePassword(@Auth AuthUser authUser, @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        userService.changePassword(authUser, userPasswordUpdateDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "비밀번호가 변경되었습니다."));
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto<String>> updateProfile(@Auth AuthUser authUser, @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        userService.updateUserProfile(authUser, userProfileUpdateDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "프로필이 수정되었습니다."));
    }

    // 토큰 재발급
    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto<String>> refreshToken(@Auth AuthUser authUser, HttpServletResponse response) {
        response.addHeader(HttpHeaders.AUTHORIZATION, userService.refreshToken(authUser));
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "토큰 재발급 되었습니다."));
    }
}
