package tave.crezipsa.crezipsa.application.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;
import tave.crezipsa.crezipsa.infrastructure.auth.client.KakaoOAuthClient;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    private final KakaoOAuthClient kakaoOAuthClient;
    private final UserRepository userRepository;

    //토큰 요청
    public Auth login(String code){
        String accessToken = kakaoOAuthClient.getAccessToken(code);

        KakaoUserInfo kakaoUserInfo = kakaoOAuthClient.getUserInfo(accessToken);

        User user = userRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() -> userRepository.save(User.createFromKakao(kakaoUserInfo)));

        return Auth.issue(user);

    }

}
