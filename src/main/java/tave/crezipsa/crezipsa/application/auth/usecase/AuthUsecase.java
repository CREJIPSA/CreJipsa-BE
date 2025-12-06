package tave.crezipsa.crezipsa.application.auth.usecase;

import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;

public interface AuthUsecase {
    public TokenResponse reissueAccessToken(Long userId, String refreshToken);
}
