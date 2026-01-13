package tave.crezipsa.crezipsa.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tave.crezipsa.crezipsa.application.auth.dto.request.RefreshTokenRequest;
import tave.crezipsa.crezipsa.application.auth.dto.response.LoginResponse;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.application.auth.usecase.AuthUsecase;
import tave.crezipsa.crezipsa.application.auth.usecase.KakaoLoginUsecase;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OAuthController {

    private final KakaoLoginUsecase kakaoLoginUsecase;
    private final AuthUsecase authUsecase;

    @GetMapping("/kakaoLogin")
    public GlobalResponseDto<LoginResponse> kakaoLoginForApp(@RequestParam String kakaoToken) {
        LoginResponse response = kakaoLoginUsecase.loginForApp(kakaoToken);

        return GlobalResponseDto.success(response);
    }

    @GetMapping("/kakao")
    public GlobalResponseDto<LoginResponse> kakaoLogin(@RequestParam String code) {
        LoginResponse response = kakaoLoginUsecase.loginForWeb(code);

        return GlobalResponseDto.success(response);
    }
    @PostMapping("/refreshToken")
    public GlobalResponseDto<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse response = authUsecase.reissueAccessToken(refreshTokenRequest);

        return GlobalResponseDto.success(response);
    }
    @DeleteMapping("/logout")
    public GlobalResponseDto logout(@AuthenticationPrincipal User user) {
        authUsecase.deleteToken(user.getUserId());

        return GlobalResponseDto.success();
    }

}
