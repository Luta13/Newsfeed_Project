package org.sparta.newsfeed.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.user.dto.*;
import org.sparta.newsfeed.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ResponseDto<String>> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "회원가입 완료되었습니다."));
    }

//    // 회원탈퇴
//    @DeleteMapping("/unregister")
//    public ResponseEntity<ResponseDto<String>> deleteAccount(@RequestParam String email, @RequestParam String password) {
//        userService.deleteAccount(email, password);
//        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Deleted user"));
//    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> loginUser(@RequestBody UserLoginDto userLoginDto , HttpServletResponse response) {
        response.addHeader("Authorization", userService.loginUser(userLoginDto));
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "로그인에 성공했습니다."));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<String>> logoutUser(@Auth AuthUser authUser) {
        userService.logoutUser(authUser.getUserId());
        return ResponseEntity.ok(new ResponseDto<>(204 , "" , "로그아웃했습니다."));
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
    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto<String>> changePassword(@Auth AuthUser authUser, @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {
        userService.changePassword(authUser, userPasswordUpdateDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Change password"));
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto<String>> updateProfile(@Auth AuthUser authUser, @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        userService.updateUserProfile(authUser, userProfileUpdateDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Update profile"));
    }
}
