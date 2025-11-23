package tave.crezipsa.crezipsa.infrastructure.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    @Value("${oauth.kakao.token-url}")
    private String tokenUrl;

    @Value("${oauth.kakao.userinfo-url}")
    private String userinfoUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient = WebClient.create();

    public String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        JsonNode response = webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null || response.get("access_token") == null || response.get("access_token").isNull()) {
            throw new IllegalStateException("카카오 응답에 access_token이 없습니다.");
        }

        return response.get("access_token").asText();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        KakaoUserInfo userInfo = webClient.get()
                .uri(userinfoUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .block();

        if (userInfo == null) {
            throw new CommonException(ErrorCode.KAKAO_USERINFO_FAILED);
        }

        return userInfo;
    }
}
