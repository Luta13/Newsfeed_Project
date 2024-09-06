package org.sparta.newsfeed.common.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JwtTokenFilter")
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();

        // 인증이 필요 없는 URL을 통과시키기 위한 처리
        if (!validatePublicUrl(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 토큰 추출
        String tokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isNotBlank(tokenValue) && tokenValue.startsWith("Bearer ")) {
            // 토큰에서 "Bearer " 부분을 제거하고 실제 토큰만 추출
            String token = tokenValue.substring(7);

            // 기본적으로 ACCESS 토큰 검증
            String type = "ACCESS";

            // 토큰 검증
            if (!jwtUtil.validateToken(token, type)) {
                log.error("인증 실패: 유효하지 않은 토큰입니다.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }

            // 토큰에서 사용자 정보 추출
            Claims claims = jwtUtil.getUserInfoFromToken(token, type);
            request.setAttribute("userId", Long.parseLong(claims.getSubject()));
            request.setAttribute("email", claims.get("email", String.class));

        } else {
            log.error("인증 실패: Authorization 헤더가 없습니다 또는 잘못된 형식입니다." + tokenValue);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Authorization 헤더가 없거나 잘못된 형식입니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean validatePublicUrl(String url) {
        // 인증이 필요 없는 URL 처리
        return !(url.equals("/users/register") || url.equals("/users/login") ||
                url.equals("/") || url.equals("/index.html") || url.equals("/login.html") || url.equals("/register.html") || url.equals("/post/newsfeed.html") ||
                url.equals("/post/createPost.html") || url.equals("/edit/Profile.html") || url.equals("/edit/change-password.html") || url.equals("/view/postView.html?id=${post.id}") ||
                url.startsWith("/static/") || url.startsWith("/css/") || url.startsWith("/js/") || url.startsWith("/img/") || url.startsWith("/view/"));
    }

}

