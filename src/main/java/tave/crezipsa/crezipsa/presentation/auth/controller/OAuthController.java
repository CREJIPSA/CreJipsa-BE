package tave.crezipsa.crezipsa.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/kakao")
    public GlobalResponseDto<LoginResponse> kakaoLogin(@RequestParam String code) {
        LoginResponse response = kakaoLoginUsecase.login(code);

        return GlobalResponseDto.success(response);
    }
    @GetMapping("/refreshToken")
    public GlobalResponseDto<TokenResponse> refreshToken(@AuthenticationPrincipal User user, @RequestParam String refreshToken) {
        TokenResponse response = authUsecase.reissueAccessToken(user.getUserId() , refreshToken);

        return GlobalResponseDto.success(response);
    }

}
