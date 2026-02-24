package tave.crezipsa.crezipsa.application.auth.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.auth.dto.request.RefreshTokenRequest;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;
import tave.crezipsa.crezipsa.global.security.TokenBlacklistService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthUsecaseImpl implements AuthUsecase {

    private final AuthRepository authRepository;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public TokenResponse reissueAccessToken(RefreshTokenRequest refreshTokenRequest) {

        tokenProvider.validateToken(refreshTokenRequest.refreshToken());

        Auth auth = authRepository.findByUserIdAndRefreshToken(refreshTokenRequest.userId(), refreshTokenRequest.refreshToken()).
                orElseThrow(() -> new CommonException(ErrorCode.INVALID_REFRESH_TOKEN));

        auth.updateAccessToken(tokenProvider.generateAccessToken(refreshTokenRequest.userId()));

        return TokenResponse.from(auth);
    }

    @Override
    public void deleteToken(Long userId) {
        authRepository.findByUserId(userId).ifPresent(auth -> {
            String accessToken = auth.getAccessToken();
            long remainingMs = tokenProvider.getRemainingExpiration(accessToken);
            tokenBlacklistService.blacklist(accessToken, remainingMs);
        });
        authRepository.deleteByUserId(userId);
    }

}
