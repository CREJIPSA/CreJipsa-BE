package tave.crezipsa.crezipsa.domain.user.command;

import tave.crezipsa.crezipsa.application.user.dto.request.UserSignUpRequest;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record UserSignUpCommand(
    String nickName,
    String email,
    Gender gender,
    boolean role,
    LocalDate birth,
    String profileImageUrl,
    String activeYoutube,
    String activeInsta,
    String activeTiktok,
    Platform mainPlatform
) {
    public static UserSignUpCommand from(UserSignUpRequest request){
        return new UserSignUpCommand(
                request.nickName(),
                request.email(),
                request.gender(),
                false,
                request.birth(),
                request.profileImageUrl(),
                request.activeYoutube(),
                request.activeInsta(),
                request.activeTiktok(),
                request.mainPlatform()
        );
    }
}
