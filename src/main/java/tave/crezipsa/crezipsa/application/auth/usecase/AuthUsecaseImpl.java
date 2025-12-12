package tave.crezipsa.crezipsa.application.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.auth.repository.AuthRepository;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.global.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthUsecaseImpl implements AuthUsecase {

    private final AuthRepository authRepository;
    private JwtTokenProvider tokenProvider;

    @Override
    public TokenResponse reissueAccessToken(Long userId, String refreshToken) {

        tokenProvider.validateToken(refreshToken);

        Auth auth = authRepository.findByUserIdAndRefreshToken(userId, refreshToken).
                orElseThrow(() -> new CommonException(ErrorCode.INVALID_REFRESH_TOKEN));

        auth.updateAccessToken(tokenProvider.generateAccessToken(userId));

        return TokenResponse.from(auth);
    }

    @Override
    public void deleteToken(Long userId) {
        authRepository.deleteByUserId(userId);
    }

}
