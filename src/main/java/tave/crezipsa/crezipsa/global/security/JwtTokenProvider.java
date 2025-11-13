package tave.crezipsa.crezipsa.global.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

// 토큰 발급, 토큰 유효성 검사 -> jwtfilter
@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "secret-key-crezipsa";
    private final long ACCESS_TOKEN_EXPIRATION = 1000L*60*60*4; //유효 4시간

    //토큰 생성
    public String generateAccessToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(userId.toString())  // 토큰이 누구거인지
                .claim("email",email)
                .setIssuedAt(new Date())        //발급 시간
                .setExpiration(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) //서명
                .compact();
    }

    //토큰 유효성 검사: 우리 서버에서 발급한 토큰/만료시간 확인, 누구의 토큰인지는 중요x
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //토큰에서 userId 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}
