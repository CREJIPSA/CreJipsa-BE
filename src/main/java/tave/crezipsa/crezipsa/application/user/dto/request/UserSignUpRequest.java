package tave.crezipsa.crezipsa.application.user.dto.request;

import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record UserSignUpRequest(
        String name,
        String nickName,
        String email,
        String password,
        Gender gender,
        LocalDate birth,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatfrom
) {
}
