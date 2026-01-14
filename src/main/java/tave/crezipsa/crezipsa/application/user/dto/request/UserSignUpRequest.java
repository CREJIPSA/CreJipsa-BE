package tave.crezipsa.crezipsa.application.user.dto.request;

import jakarta.validation.constraints.*;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;
import java.util.List;

public record UserSignUpRequest(

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2~10자로 입력해주세요.")
        String nickName,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        Gender gender,
        LocalDate birth,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatform,
        List<String> userInterest
) {
}
