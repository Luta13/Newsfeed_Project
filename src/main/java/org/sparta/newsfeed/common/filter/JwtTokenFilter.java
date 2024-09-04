package org.sparta.newsfeed.common.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sparta.newsfeed.common.jwt.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j(topic = "JwtTokenFilter")
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        if (Strings.isNotBlank(url) && validateNotPublicUrl(url)) {
            // 나머지 API 요청은 인증 처리 진행
            // 토큰 확인
            String tokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (Strings.isNotBlank(tokenValue)) { // 토큰이 존재하면 검증 시작
                // 토큰 검증
                String token = jwtUtil.substringToken(tokenValue);
                if (!jwtUtil.validateToken(token , "ACCESS")) {
                    log.error("인증 실패");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "인증에 실패했습니다.");
                } else {
                    log.info("토큰 검증 성공");
                    Claims claims = jwtUtil.getUserInfoFromToken(token , "ACCESS");

                    HttpServletRequest httpRequest = (HttpServletRequest) request;

                    // 사용자 정보를 ArgumentResolver 로 넘기기 위해 HttpServletRequest 에 세팅
                    httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
                    httpRequest.setAttribute("email", claims.get("email", String.class));

                }
            } else {
                log.error("토큰이 없습니다.");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST , "토큰이 없습니다.");
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateNotPublicUrl(String url) {
        return !(url.equals("/users/register") || url.equals("/users/login") || url.equals("/users/refresh-token"));
    }
}