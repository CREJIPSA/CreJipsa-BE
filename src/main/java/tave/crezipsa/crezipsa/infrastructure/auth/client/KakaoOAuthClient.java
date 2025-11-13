package tave.crezipsa.crezipsa.infrastructure.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

//DDD관점 외부 시스템 연동
//카카오 api 호출 요청 담당
@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    @Value("${oauth.kako.token-url}")
    private String tokenUrl;

    @Value("{oauth.kakao.userinfo-url}")
    private String userinfoUrl;

    @Value("{oauth.kakao.Client-id}")
    private String clientId;

    @Value("{oauth.kakao.redirect-uri}")
    private String redirectUri;

   private final WebClient webClient = WebClient.create();

   public String getAccessToken(String code) {
       return webClient.post()
               .uri(tokenUrl)
               .bodyValue("grant_type=authorization_code&client_id=" + clientId +
                       "&redirect_uri=" + redirectUri + "&code=" + code)
               .retrieve()
               .bodyToMono(String.class)
               .block();
   }
   public KakaoUserInfo getUserInfo(String accessToken){
       return webClient.get()
               .uri(userinfoUrl)
               .header("Authorization"+ "Bearer"+accessToken )
               .retrieve()
               .bodyToMono(KakaoUserInfo.class)
               .block();
   }
}
