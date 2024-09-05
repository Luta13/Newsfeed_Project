package org.sparta.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.common.dto.AuthUser;
import org.sparta.newsfeed.common.exception.custom.BadRequestException;
import org.sparta.newsfeed.common.exception.custom.ConflictException;
import org.sparta.newsfeed.common.exception.code.ErrorCode;
import org.sparta.newsfeed.common.config.JwtUtil;
import org.sparta.newsfeed.friend.repository.FriendRepository;
import org.sparta.newsfeed.user.dto.*;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.entity.UserStatusEnum;
import org.sparta.newsfeed.user.exception.UserInvalidException;
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
            throw new ConflictException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        // 비밀번호 정규식 검증
        if (!passwordEncoder.passwordVerification(userRegisterDto.getPassword())) {
            throw new UserInvalidException(ErrorCode.INVALID_PASSWORD_FORMAT);
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
            throw new UserInvalidException(ErrorCode.EMAIL_NOT_MATCHED);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(userUnregisterDto.getPassword(), user.getPassword())) {
            throw new UserInvalidException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 이미 탈퇴한 사용자 확인
        if (user.getStatus() == UserStatusEnum.REMOVE) {
            throw new BadRequestException(ErrorCode.USER_ALREADY_WITHDRAWN);
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
            throw new UserInvalidException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 새로운 비밀번호가 현재 비밀번호와 동일한지 확인
        if (passwordEncoder.matches(userPasswordUpdateDto.getChangePassword(), user.getPassword())) {
            throw new UserInvalidException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        // 새로운 비밀번호 형식 검증
        if (!passwordEncoder.passwordVerification(userPasswordUpdateDto.getChangePassword())) {
            throw new UserInvalidException(ErrorCode.INVALID_PASSWORD_FORMAT);
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
        User user = userRepository.findByEmail(userLoginDto.getEmail()).orElseThrow(() -> new UserInvalidException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new UserInvalidException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }

        // refresh token
        List<String> list = new ArrayList<>();
        list.add(jwtUtil.createToken(user.getUserId(), user.getEmail(), JwtUtil.ACCESS));
        list.add(jwtUtil.createToken(user.getUserId(), user.getEmail(), JwtUtil.REFRESH));
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
        return jwtUtil.createToken(user.getUserId(), user.getEmail(), JwtUtil.ACCESS);
    }
}
