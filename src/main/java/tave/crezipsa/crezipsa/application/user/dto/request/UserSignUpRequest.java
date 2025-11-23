package tave.crezipsa.crezipsa.application.user.dto.request;

import jakarta.validation.constraints.*;
import tave.crezipsa.crezipsa.domain.user.enums.Gender;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

import java.time.LocalDate;

public record UserSignUpRequest(

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2~10자로 입력해주세요.")
        String nickName,

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{1,20}$",
                message = "비밀번호는 영어, 숫자, 특수문자를 각각 1개 이상 포함하고 20자 이내여야 합니다."
        )
        String password,

        Gender gender,
        LocalDate birth,
        String activeYoutube,
        String activeTiktok,
        String activeInsta,
        Platform mainPlatfrom
) {
}
