package tave.crezipsa.crezipsa.domain.user.entity;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import lombok.*;

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
}
