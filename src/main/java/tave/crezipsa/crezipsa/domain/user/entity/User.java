package tave.crezipsa.crezipsa.domain.user.entity;
import tave.crezipsa.crezipsa.domain.user.command.UserSignUpCommand;
import tave.crezipsa.crezipsa.domain.user.command.UserUpdateCommand;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import lombok.*;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

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
        private String activeYoutube;
        private String activeTiktok;
        private String activeInsta;
        private Platform mainPlatform;

        public static User createFromUser(UserSignUpCommand cmd) {

            return User.builder()
                .nickName(cmd.nickName())
                .email(cmd.email())
                .gender(cmd.gender())
                .role(false)
                .birth(cmd.birth())
                .activeYoutube(cmd.activeYoutube())
                .activeInsta(cmd.activeInsta())
                .activeTiktok(cmd.activeTiktok())
                .mainPlatform(cmd.mainPlatform())
                .build();
        }
        public void updateFromUser(UserUpdateCommand cmd) {

            if(cmd.getActiveYoutube() != null) { this.activeYoutube = cmd.getActiveYoutube(); }
            if(cmd.getActiveInsta() != null) { this.activeInsta = cmd.getActiveInsta() ;}
            if(cmd.getActiveTiktok() != null) { this.activeTiktok = cmd.getActiveTiktok(); }
            if(cmd.getMainPlatform() != null) { this.mainPlatform = cmd.getMainPlatform(); }

        }

       public String getMainPlatformAccount() {
           if (mainPlatform == null) {
            return null;
          }

           return switch (mainPlatform) {
            case YOUTUBE -> activeYoutube;
            case INSTAGRAM -> activeInsta;
            case TIKTOK -> activeTiktok;
           };
    }


}

