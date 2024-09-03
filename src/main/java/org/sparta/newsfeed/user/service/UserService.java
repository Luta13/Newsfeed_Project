package org.sparta.newsfeed.user.service;

import lombok.RequiredArgsConstructor;
import org.sparta.newsfeed.common.config.PasswordEncoder;
import org.sparta.newsfeed.common.jwt.JwtUtil;
import org.sparta.newsfeed.user.dto.UserLoginDto;
import org.sparta.newsfeed.user.entity.User;
import org.sparta.newsfeed.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
}
