package tave.crezipsa.crezipsa.application.auth.dto.response;

import ch.qos.logback.core.subst.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tave.crezipsa.crezipsa.domain.auth.entity.Auth;

public record TokenResponse(
        Long userId,
        String accessToken,
        String refreshToken

){
    public static TokenResponse from(Auth auth) {

       return new TokenResponse(
               auth.getUserId(),
               auth.getAccessToken(),
               auth.getRefreshToken());
    }
}
