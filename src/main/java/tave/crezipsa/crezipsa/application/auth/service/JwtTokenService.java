package tave.crezipsa.crezipsa.application.auth.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;

@RequiredArgsConstructor
@Service
//다시 보기
public class JwtTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepository authRepository;

    public TokenResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());

        // Refresh Token 저장
        authRepository.save(Auth.builder()
                .userId(user.getUserId())
                .refreshToken(refreshToken)
                .build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .build();
    }
}
