package tave.crezipsa.crezipsa.application.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private boolean isNewUser;
    private User user;
    private String accessToken;
    private String refreshToken;
    private KakaoUserInfo kakaoUserInfo;

    public static LoginResponse needsSignup(KakaoUserInfo info) {
        return new LoginResponse(true, null, null, null, info);
    }

    public static LoginResponse success(User user, String accessToken, String refreshToken) {
        return new LoginResponse(false, user, accessToken, refreshToken, null);
    }
}
