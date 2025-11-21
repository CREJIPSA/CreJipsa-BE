package tave.crezipsa.crezipsa.global.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 토큰 발급, 토큰 유효성 검사 -> jwtfilter
@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final Long accessExpireMs;
    private final Long refreshExpireMs;

    public JwtTokenProvider(
        @Value("${jwt-secret-key}") String secret,
        @Value("${jwt-access-expire-ms}") long accessExpireMs,
        @Value("${jwt-refresh-expire-ms}") long refreshExpireMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpireMs = accessExpireMs;
        this.refreshExpireMs = refreshExpireMs;
    }

    //토큰 생성
    public String generateAccessToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(userId.toString())  // 토큰이 누구거인지
                .claim("email",email)
                .setIssuedAt(new Date())        //발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+ accessExpireMs))
                .signWith(secretKey, SignatureAlgorithm.HS256) //서명 키 바꾸기 **
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpireMs))
                .signWith(SignatureAlgorithm.HS256, secretKey )
                .compact();
    }

    //토큰 유효성 검사: 우리 서버에서 발급한 토큰/만료시간 확인, 누구의 토큰인지는 중요x
    public void validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CommonException(ErrorCode.INVALID_TOKEN);
        }
    }

    //토큰에서 userId 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

}
