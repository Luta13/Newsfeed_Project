package org.sparta.newsfeed.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    // CORS 설정 추가
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:3000")  // 프론트엔드 도메인 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 메서드
                .allowedHeaders("Authorization", "Content-Type")  // 허용할 헤더
                .exposedHeaders("Authorization", "access_token")  // 노출할 헤더 명시
                .allowCredentials(true);  // 인증 정보 허용 (쿠키 등)
    }
}
