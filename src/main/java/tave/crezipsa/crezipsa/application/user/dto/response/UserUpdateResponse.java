package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public record UserUpdateResponse(
        Long userId,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatform
) {
}
