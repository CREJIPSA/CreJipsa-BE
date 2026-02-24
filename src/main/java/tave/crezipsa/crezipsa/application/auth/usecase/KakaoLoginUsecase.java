package tave.crezipsa.crezipsa.application.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tave.crezipsa.crezipsa.application.auth.dto.response.LoginResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;
import tave.crezipsa.crezipsa.infrastructure.auth.client.KakaoOAuthClient;

@Service
@RequiredArgsConstructor
public class KakaoLoginUsecase {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    //앱방식 로그인
    public LoginResponse loginForApp(String kakaoToken) {
        KakaoUserInfo kakaoUserInfo = kakaoOAuthClient.getUserInfo(kakaoToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElse(null);

        if (user == null) {
            return LoginResponse.needsSignup(kakaoUserInfo);
        }
        if(user.isRole()) {
            throw new CommonException(ErrorCode.USER_INVALID_ROLE);
        }

        // 이메일도 존재하고//새로운 유저가 아닐때 바로 토큰 발급
        String jwt = jwtTokenProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        Auth auth = authRepository.findByUserId(user.getUserId())
                .orElseGet(() -> Auth.builder()
                        .userId(user.getUserId())
                        .providerUserId(String.valueOf(kakaoUserInfo.getId()))
                        .build()
                );

        auth.updateTokens(jwt, refreshToken);
        authRepository.save(auth);

        return LoginResponse.success(jwt, refreshToken, kakaoUserInfo);
    }

    //웹 방식 카카오 로그인
    public LoginResponse loginForWeb(String code) {

        String kakaoToken = kakaoOAuthClient.getAccessToken(code);
        KakaoUserInfo kakaoUserInfo = kakaoOAuthClient.getUserInfo(kakaoToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElse(null);

        if (user == null) {
            return LoginResponse.needsSignup(kakaoUserInfo);
        }
        if(user.isRole()) {
            throw new CommonException(ErrorCode.USER_INVALID_ROLE);
        }

        // 이메일도 존재하고//새로운 유저가 아닐때 바로 토큰 발급
        String jwt = jwtTokenProvider.generateAccessToken(user.getUserId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        Auth auth = authRepository.findByUserId(user.getUserId())
                .orElseGet(() -> Auth.builder()
                        .userId(user.getUserId())
                        .providerUserId(String.valueOf(kakaoUserInfo.getId()))
                        .build()
                );

        auth.updateTokens(jwt, refreshToken);
        authRepository.save(auth);

        return LoginResponse.success(jwt, refreshToken, kakaoUserInfo);
    }
}
