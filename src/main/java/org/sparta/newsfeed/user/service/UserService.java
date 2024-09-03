package org.sparta.newsfeed.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 등록
    @Transactional
    public void registerUser(String email, String password) {
        // 비밀번호 정규식 검증
        if (!passwordEncoder.passwordVerification(password)) {
            throw new IllegalArgumentException("비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하며, 최소 8글자 이상이어야 합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 사용자 저장
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    // 사용자 인증
    public boolean authenticateUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

}
