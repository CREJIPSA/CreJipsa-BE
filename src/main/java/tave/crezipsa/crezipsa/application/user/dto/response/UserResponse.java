package tave.crezipsa.crezipsa.application.user.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record UserResponse(
        Long userId,
        String nickName,
        String email,
        Gender gender,
        String profileImageUrl,
        LocalDate birth,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatform
) {
    public static UserResponse from(User u) {
        return new UserResponse(
                u.getUserId(),
                u.getNickName(),
                u.getEmail(),
                u.getGender(),
                u.getProfileImageUrl(),
                u.getBirth(),
                u.getActiveYoutube(),
                u.getActiveTiktok(),
                u.getActiveInsta(),
                u.getMainPlatform()
        );
    }
}
