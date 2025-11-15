package tave.crezipsa.crezipsa.infrastructure.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 응답에 우리가 안 쓰는 필드 많아서 무시
public class KakaoUserInfo {

    private Long id;

    // 카카오에서 들어올 때만 쓰고, 프론트로 응답 보낼 땐 숨기기
    @JsonProperty(value = "kakao_account", access = JsonProperty.Access.WRITE_ONLY)
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {

        private String email;   // kakao_account.email

        @JsonProperty("profile")
        private KakaoProfile profile; // kakao_account.profile
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoProfile {

        private String nickname; // kakao_account.profile.nickname

        @JsonProperty("profile_image_url")
        private String profileImageUrl; // kakao_account.profile.profile_image_url
    }

    // 👉 여기서 이메일 반환 (응답 JSON에도 "email" 로 나감)
    @JsonProperty("email")
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.getEmail() : null;
    }

    @JsonProperty("nickname")
    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getNickname();
    }

    @JsonProperty("profileImage")
    public String getProfileImage() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getProfileImageUrl();
    }
}
