package tave.crezipsa.crezipsa.domain.user.entity;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import lombok.*;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class User {
        private Long userId;
        private String name;
        private String nickName;
        private String email;
        private String password;
        private Gender gender;
        private String profileImageUrl;
        private boolean role;
        private LocalDate dateOfBirth;
        private String activeYotube;
        private String activeTikTok;
        private String activeInsta;

    public static User createFromKakao(KakaoUserInfo kakaoUserInfo) {
            return User.builder()
                    .email(kakaoUserInfo.getEmail())
                    .name(kakaoUserInfo.getNickname()) // 카카오는 보통 닉네임만 제공
                    .profileImageUrl(kakaoUserInfo.getProfileImage())
                    .build();
    }
}

