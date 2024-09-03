package org.sparta.newsfeed.common.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_PREFIX = "auth";
    public static final String BEARER_PREFIX = "Bearer ";

    private final long TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = Logger.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String value) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(value) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_HEADER, AUTHORIZATION_PREFIX)
                        .setExpiration(new Date(date.getTime() + TOKEN_EXPIRATION_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 토큰에서 사용자 정보 추출
    public String getUserInfoFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject(); // 토큰의 Subject에서 사용자 식별자값(ID)을 반환
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true; // 유효한 토큰
        } catch (SecurityException | MalformedJwtException e) {
            logger.warning("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.warning("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.warning("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.warning("JWT 토큰이 잘못되었습니다.");
        }
        return false; // 유효하지 않은 토큰
    }

    // 토큰에서 클레임 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 토큰이 만료되었을 경우 클레임을 반환
        }
    }
}


