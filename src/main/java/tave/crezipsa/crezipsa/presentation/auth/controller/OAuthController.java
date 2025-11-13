package tave.crezipsa.crezipsa.presentation.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tave.crezipsa.crezipsa.application.auth.service.KakaoLoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {
    private final KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {

    }


}
