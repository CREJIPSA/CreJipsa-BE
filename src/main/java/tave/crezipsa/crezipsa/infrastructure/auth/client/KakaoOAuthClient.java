package tave.crezipsa.crezipsa.infrastructure.auth.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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

    // 필요하면 client-secret도
    // @Value("${oauth.kakao.client-secret}")
    // private String clientSecret;

    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);
        // 카카오 앱 설정에서 "Client Secret 사용" 이면 이거도 꼭 추가
        // formData.add("client_secret", clientSecret);

        try {
            String responseBody = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("### Kakao Token Raw Response: " + responseBody);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode accessTokenNode = root.get("access_token");

            if (accessTokenNode == null || accessTokenNode.isNull()) {
                throw new IllegalStateException("카카오 응답에 access_token이 없습니다.");
            }

            String accessToken = accessTokenNode.asText();
            System.out.println("### Kakao access_token: " + accessToken);

            return accessToken;

        } catch (WebClientResponseException e) {
            System.out.println("### Kakao Token Error Status: " + e.getStatusCode());
            System.out.println("### Kakao Token Error Body: " + e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            System.out.println("### Kakao Token Parsing Error: " + e.getMessage());
            throw new RuntimeException("카카오 토큰 파싱 중 오류", e);
        }
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        try {
            KakaoUserInfo userInfo = webClient.get()
                    .uri(userinfoUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(KakaoUserInfo.class)
                    .block();

            System.out.println("### Kakao UserInfo: " + userInfo);
            return userInfo;

        } catch (WebClientResponseException e) {
            System.out.println("### Kakao UserInfo Error Status: " + e.getStatusCode());
            System.out.println("### Kakao UserInfo Error Body: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}
