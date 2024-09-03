package org.sparta.newsfeed.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.access.key}")
    private String secretAccessKey;
    private Key accessKey;

    private final long TOKEN_ACCESS_TIME = 60 * 5 * 1000L;

    @Value("${jwt.secret.refresh.key}")
    private String secretRefreshKey;
    private Key refreshKey;

    private final long TOKEN_REFRESH_TIME = 60 * 60 * 5 * 1000L;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        accessKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretAccessKey));
        refreshKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretRefreshKey));
    }

    // 토큰 생성
    public String createToken(Long userId , String email , String type) {
        Date date = new Date();

        Key key = type.equals("ACCESS") ? accessKey : refreshKey;
        long time = type.equals("ACCESS") ? TOKEN_ACCESS_TIME : TOKEN_REFRESH_TIME;

        return BEARER_PREFIX +
                Jwts.builder()
                    .setSubject(String.valueOf(userId)) // 사용자 식별자값(ID)
                    .claim("email" , email)
                    .setExpiration(new Date(date.getTime() + time)) // 만료 시간
                    .setIssuedAt(date) // 발급일
                    .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                    .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token , String type) {
        try {
            Key key = type.equals("ACCESS") ? accessKey : refreshKey;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // 유효한 토큰
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 유효하지 않은 토큰
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            // "Bearer "이 공백을 포함하여 7자를 자른다.
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token , String type) {
        Key key = type.equals("ACCESS") ? accessKey : refreshKey;
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}


