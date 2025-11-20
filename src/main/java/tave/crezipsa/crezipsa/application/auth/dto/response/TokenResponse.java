package tave.crezipsa.crezipsa.application.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String email
){}
