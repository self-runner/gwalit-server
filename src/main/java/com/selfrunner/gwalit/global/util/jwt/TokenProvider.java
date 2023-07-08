package com.selfrunner.gwalit.global.util.jwt;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private static final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 1000L; // 1시간
    private static final long REFRESH_TOKEN_VALID_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일

    // AccessToken 발급
    private String createAccessToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getMemberId());
        claims.put("type", member.getType());

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("Access")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 발급
    private String createRefreshToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getMemberId());
        claims.put("type", member.getType());

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("Refresh")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // 로그인 시, atk, rtk 모두 발급
    public TokenDto generateToken(Member member) {
        String atk = createAccessToken(member);
        String rtk = createRefreshToken(member);

        return TokenDto.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .build();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) { // 만료된 토큰일 경우
            // atk 만료인 경우


            // rtk 만료인 경우
            throw new RuntimeException(ErrorCode.EXPIRE_ACCESS_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) { // 지원하지 않는 토큰인 경우
            throw new RuntimeException(e);
        }
    }

    // 해당 토큰의 id 반환
    public Long getId(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        return Long.valueOf(claims.get("id").toString());
    }

    // 해당 토큰의 type 반환
    public String getType(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

        return claims.get("type").toString();
    }
}