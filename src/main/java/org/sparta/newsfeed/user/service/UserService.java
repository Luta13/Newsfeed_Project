package org.sparta.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.dto.*;
import org.sparta.newsfeed.common.jwt.JwtUtil;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.entity.UserStatusEnum;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final FriendRepository friendRepository;

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
        User user = new User(userRegisterDto.getEmail(), encodedPassword, userRegisterDto.getName(), UserStatusEnum.ACTIVE);
        userRepository.save(user);
    }

    // 회원 탈퇴
    public void deleteAccount(AuthUser authUser, UserUnregisterDto userUnregisterDto) {
        // 사용자 조회
        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());

        if (!user.getEmail().equals(authUser.getEmail())) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(userUnregisterDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이미 탈퇴한 사용자 확인
        if (user.getStatus() == UserStatusEnum.REMOVE) {
            throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
        }

        friendRepository.deleteByBaseIdOrFriendId(user, user);

        // 사용자 상태를 탈퇴 상태로 변경
        user.updateStatus(UserStatusEnum.REMOVE);
        userRepository.save(user);
    }

    // 비밀번호 변경
    public void changePassword(AuthUser authUser, UserPasswordUpdateDto userPasswordUpdateDto) {
        // 사용자 조회
        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(userPasswordUpdateDto.getOriginalPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호가 현재 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(userPasswordUpdateDto.getChangePassword(), user.getPassword())) {
            throw new IllegalArgumentException("새로운 비밀번호는 현재 비밀번호와 동일할 수 없습니다.");
        }

        // 새로운 비밀번호 형식 검증
        if (!passwordEncoder.passwordVerification(userPasswordUpdateDto.getChangePassword())) {
            throw new IllegalArgumentException("비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다.");
        }

        // 새로운 비밀번호 암호화 및 저장
        String encodedNewPassword = passwordEncoder.encode(userPasswordUpdateDto.getChangePassword());
        user.changePassword(encodedNewPassword);
        userRepository.save(user);
    }

    // 프로필 수정
    public void updateUserProfile(AuthUser authUser, UserProfileUpdateDto userProfileUpdateDto) {
        // 사용자 조회
        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());

        // 이름 수정
        user.updateName(userProfileUpdateDto.getName());

        // 사용자 정보 저장
        userRepository.save(user);
    }

    public List<String> loginUser(UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 맞지 않습니다."));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 맞지 않습니다.");
        }

        // refresh token
        List<String> list = new ArrayList<>();
        list.add(jwtUtil.createToken(user.getUserId(), user.getEmail(), "ACCESS"));
        list.add(jwtUtil.createToken(user.getUserId(), user.getEmail(), "REFRESH"));
        return list;
    }

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return new UserProfileDto(user.getEmail(), user.getName());
    }

    public UserProfileDto getUserProfile(String email) {
        User user = userRepository.findByEmailOrElseThrow(email);
        return new UserProfileDto(user.getEmail(), user.getName());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmailOrElseThrow(email);
    }

    public User findById(Long userId) {
        return userRepository.findByIdOrElseThrow(userId);
    }

    public String refreshToken(AuthUser authUser) {
        User user = userRepository.findByIdOrElseThrow(authUser.getUserId());
        return jwtUtil.createToken(user.getUserId(), user.getEmail(), "ACCESS");
    }
}
