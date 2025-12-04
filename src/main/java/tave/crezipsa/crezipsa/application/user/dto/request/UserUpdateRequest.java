package tave.crezipsa.crezipsa.application.user.dto.request;

import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record UserUpdateRequest(
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatform
) {
}
