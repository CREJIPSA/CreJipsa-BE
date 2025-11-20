package tave.crezipsa.crezipsa.application.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private boolean isNewUser;
    private String accessToken;
    private String refreshToken;
    private KakaoUserInfo kakaoUserInfo;

    public static LoginResponse needsSignup(KakaoUserInfo info) {
        return new LoginResponse(true, null,null, info);
    }

    public static LoginResponse success(String accessToken, String refreshToken, KakaoUserInfo kakaoUserInfo) {
        return new LoginResponse(false,accessToken, refreshToken, kakaoUserInfo);
    }
}
