package org.sparta.newsfeed.user.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("Registered user");
    }

    // 회원탈퇴
    @DeleteMapping("/unregister")
    public ResponseEntity<String> deleteAccount() {
        return ResponseEntity.ok("Deleted user");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> loginUser() {
        return ResponseEntity.ok("Login user");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok("Logout user");
    }

    // 자신의 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok("Profile");
    }

    // 다른 사용자 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(@RequestParam String email) {
        return ResponseEntity.ok("Profile");
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword() {
        return ResponseEntity.ok("Change password");
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<String> updateProfile() {
        return ResponseEntity.ok("Update profile");
    }
}
