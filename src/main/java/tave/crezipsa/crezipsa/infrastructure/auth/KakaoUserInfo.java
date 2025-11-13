package tave.crezipsa.crezipsa.infrastructure.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfo {

    private Long id;
    private String email;
    private String nickname;
    private String profileImage;

    public KakaoUserInfo(Long id, String email, String nickname, String profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

}
