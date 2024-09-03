package org.sparta.newsfeed.user.controller;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.annotation.Auth;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.dto.ResponseDto;
import org.sparta.newsfeed.user.dto.UserLoginDto;
import org.sparta.newsfeed.user.dto.UserRegisterDto;
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

    // 회원탈퇴
    @DeleteMapping("/unregister")
    public ResponseEntity<ResponseDto<String>> deleteAccount() {
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Deleted user"));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> loginUser(@RequestBody UserLoginDto userLoginDto) {
        userService.loginUser(userLoginDto);
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Login user"));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<String>> logoutUser() {
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Logout user"));
    }

    // 자신의 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto<String>> getProfile(@Auth AuthUser authUser) {
        System.out.println(authUser.getUserId() + " " + authUser.getEmail());
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Profile"));
    }

    // 다른 사용자 프로필 조회
    @GetMapping("/profile/user")
    public ResponseEntity<ResponseDto<String>> getUserProfile(@RequestParam("email") String email) {
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Profile"));
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
    public ResponseEntity<ResponseDto<String>> changePassword() {
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Change password"));
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto<String>> updateProfile() {
        return ResponseEntity.ok(new ResponseDto<>(200 , "" , "Update profile"));
    }
}
