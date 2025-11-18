package tave.crezipsa.crezipsa.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tave.crezipsa.crezipsa.application.auth.dto.response.LoginResponse;
import tave.crezipsa.crezipsa.application.auth.dto.response.TokenResponse;
import tave.crezipsa.crezipsa.application.auth.service.KakaoLoginService;
import tave.crezipsa.crezipsa.global.common.dto.GlobalResponseDto;

//@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao")
    public GlobalResponseDto<LoginResponse> kakaoLogin(@RequestParam String code) {
        LoginResponse response = kakaoLoginService.login(code);

        return GlobalResponseDto.success(response);
    }

}
