package org.sparta.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void test() {
        String password = "test";

        // 비밀번호 암호화
        String encode = passwordEncoder.encode(password);
        System.out.println(encode);

        // 비밀번호가 일치하는지 검증
        System.out.println(passwordEncoder.matches(password , encode));

        // 비밀번호 정규식 검증
        if(passwordEncoder.passwordVerification(password)) {
            System.out.println("Password verified");
        }
    }
}
