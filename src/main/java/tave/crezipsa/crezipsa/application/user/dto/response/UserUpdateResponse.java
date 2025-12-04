package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public record UserUpdateResponse(
        Long userId,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatform

) {
    public static UserUpdateResponse from(User user) {
        return new UserUpdateResponse(
                user.getUserId(),
                user.getActiveYoutube(),
                user.getActiveTiktok(),
                user.getActiveInsta(),
                user.getMainPlatform()
        );
    }
}
