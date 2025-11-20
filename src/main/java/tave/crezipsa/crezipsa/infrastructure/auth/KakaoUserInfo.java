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

        private String nickname;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }

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
