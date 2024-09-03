package org.sparta.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.user.dto.UserRegisterDto;
import org.sparta.newsfeed.common.jwt.JwtUtil;
import org.sparta.newsfeed.user.dto.UserLoginDto;
import org.sparta.newsfeed.user.dto.UserProfileDto;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.entity.UserStatusEnum;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 사용자 등록
    public void registerUser(UserRegisterDto userRegisterDto) {
        // 중복된 이메일 확인
        if (userRepository.findByEmail(userRegisterDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 정규식 검증
        if (!passwordEncoder.passwordVerification(userRegisterDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());

        // 사용자 저장
        User user = new User(userRegisterDto.getEmail(), encodedPassword, userRegisterDto.getName(), "", UserStatusEnum.ACTIVE);
        userRepository.save(user);
    }

    public String loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 맞지 않습니다."));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 맞지 않습니다.");
        }

        // refresh token
        String refresh = jwtUtil.createToken(user.getUserId() , user.getEmail() , "REFRESH");
        user.updateToken(refresh);
        userRepository.save(user);

        return jwtUtil.createToken(user.getUserId() , user.getEmail() , "ACCESS");
    }

    public void logoutUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("조회 도중 에러가 발생했습니다."));
        user.updateToken("");
        userRepository.save(user);
    }

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("조회 도중 에러가 발생했습니다."));
        return new UserProfileDto(user.getEmail() , user.getName());
    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 회원 정보입니다."));
        return new UserProfileDto(user.getEmail() , user.getName());
    }
}
