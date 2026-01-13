package tave.crezipsa.crezipsa.application.auth.usecase;

import tave.crezipsa.crezipsa.application.auth.dto.request.RefreshTokenRequest;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;

public interface AuthUsecase {
    public TokenResponse reissueAccessToken(RefreshTokenRequest refreshTokenRequest);
    public void deleteToken(Long userId);
}
