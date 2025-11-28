package tave.crezipsa.crezipsa.domain.user.entity;
import org.apache.coyote.Request;
import tave.crezipsa.crezipsa.domain.user.command.CreateUserCommand;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import lombok.*;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.infrastructure.auth.KakaoUserInfo;

import java.time.LocalDate;
import java.util.Date;

@Getter
@AllArgsConstructor
@Builder
public class User {
        private Long userId;
        private String nickName;
        private String email;
        private Gender gender;
        private String profileImageUrl;
        private boolean role;
        private LocalDate birth;
        private String activeYotube;
        private String activeTiktok;
        private String activeInsta;
        private Platform mainPlatform;

    public static User createFromKakao(KakaoUserInfo kakaoUserInfo) {
            return User.builder()
                    .email(kakaoUserInfo.getEmail())
                    .nickName(kakaoUserInfo.getNickname())
                    .profileImageUrl(kakaoUserInfo.getProfileImage())
                    .build();
    }
    public static User createFromUser(CreateUserCommand cmd) {
        return  User.builder()
                .nickName(cmd.nickName())
                .email(cmd.email())
                .gender(cmd.gender())
                .role(true)
                .birth(cmd.birth())
                .activeYotube(cmd.activeYoutube())
                .activeInsta(cmd.activeInsta())
                .activeTiktok(cmd.activeTiktok())
                .mainPlatform(cmd.mainPlatform())
                .build();
        }
}

