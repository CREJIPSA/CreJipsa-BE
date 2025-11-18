package tave.crezipsa.crezipsa.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tave.crezipsa.crezipsa.application.auth.dto.response.LoginResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;
import tave.crezipsa.crezipsa.infrastructure.auth.client.KakaoOAuthClient;

//@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    public LoginResponse login(String code) {

        String accessToken = kakaoOAuthClient.getAccessToken(code);
        KakaoUserInfo kakaoUserInfo = kakaoOAuthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElse(null);

        if (user == null) {
            return LoginResponse.needsSignup(kakaoUserInfo);
        }

        String jwt = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        Auth auth = authRepository.findByUserId(user.getUserId())
                .orElseGet(() -> Auth.builder()
                        .userId(user.getUserId())
                        .providerUserId(String.valueOf(kakaoUserInfo.getId()))
                        .build()
                );

        auth.updateTokens(accessToken, refreshToken);

        authRepository.save(auth);
        return LoginResponse.success(jwt, refreshToken, kakaoUserInfo);
    }
}
