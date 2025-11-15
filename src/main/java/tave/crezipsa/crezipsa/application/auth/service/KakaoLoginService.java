package tave.crezipsa.crezipsa.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tave.crezipsa.crezipsa.application.auth.dto.response.LoginResponse;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;
import tave.crezipsa.crezipsa.infrastructure.auth.client.KakaoOAuthClient;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(String code) {

        // 1) 카카오 Access Token 받기
        String accessToken = kakaoOAuthClient.getAccessToken(code);

        // 2) 카카오 유저 정보 받기
        KakaoUserInfo kakaoUserInfo = kakaoOAuthClient.getUserInfo(accessToken);

        // 3) 이메일로 기존 회원 조회
        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElse(null);

        // 4) 기존 회원이 없으면 → 회원가입 단계 필요
        if (user == null) {
            return LoginResponse.needsSignup(kakaoUserInfo);
        }

        // 5) 기존 회원이면 → JWT 발급
        String jwt = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        return LoginResponse.success(user, jwt, refreshToken);
    }
}
