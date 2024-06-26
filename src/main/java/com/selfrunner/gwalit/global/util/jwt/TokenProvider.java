
package com.selfrunner.gwalit.global.util.jwt;

import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.exception.ApplicationException;
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

    private static final long ACCESS_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    private static final long REFRESH_TOKEN_VALID_TIME = 30 * 24 * 60 * 60 * 1000L; // 30일

    // AccessToken 발급
    private String createAccessToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("phone", member.getPhone());
        claims.put("type", member.getType());

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject(member.getPhone())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // RefreshToken 발급
    private String createRefreshToken(Member member) {
        Claims claims = Jwts.claims();
        claims.put("phone", member.getPhone());
        claims.put("type", member.getType());

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject(member.getPhone())
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

    // 토큰 재발급
    public TokenDto regenerateToken(Member member, String rtk) {
        String atk = createAccessToken(member);

        TokenDto tokenDto = TokenDto.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .build();

        long expirationDate = getExpiration(rtk);
        Date currentDate = new Date();
        if(expirationDate - currentDate.getTime() <=  ACCESS_TOKEN_VALID_TIME) {
            String reissueRtk = createRefreshToken(member);
            tokenDto.setRefreshToken(reissueRtk);
        }

        return tokenDto;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) { // 만료된 토큰일 경우
            throw new ApplicationException(ErrorCode.EXPIRE_TOKEN);
        } catch (UnsupportedJwtException e) { // 지원하지 않는 토큰인 경우
            throw new ApplicationException(ErrorCode.WRONG_TOKEN);
        }
    }

    // 해당 토큰의 id 반환
    public String getPhone(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();

        return claims.get("phone").toString();
    }

    // 해당 토큰의 type 반환
    public String getType(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.get("type").toString();
    }

    // 해당 토큰의 Expiration 반환
    public Long getExpiration(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
        return claims.getExpiration().getTime();
    }
}