package org.sparta.newsfeed.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.user.dto.UserRegisterDto;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.entity.UserStatusEnum;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        User user = new User(userRegisterDto.getEmail(), encodedPassword , userRegisterDto.getName() , "" , UserStatusEnum.ACTIVE);
        userRepository.save(user);
    }
}
