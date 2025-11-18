package tave.crezipsa.crezipsa.global.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

// 토큰 발급, 토큰 유효성 검사 -> jwtfilter
@Component
public class JwtTokenProvider {

    private final Key secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    private final long ACCESS_TOKEN_EXPIRATION = 1000L*60*60*4; //유효 4시간
    private final long REFRESH_TOKEN_EXPIRATION = 1000L*60*60*10;
    //토큰 생성
    public String generateAccessToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(userId.toString())  // 토큰이 누구거인지
                .claim("email",email)
                .setIssuedAt(new Date())        //발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256) //서명 키 바꾸기 **
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, secretKey )
                .compact();
    }

    //토큰 유효성 검사: 우리 서버에서 발급한 토큰/만료시간 확인, 누구의 토큰인지는 중요x
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey ).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
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
