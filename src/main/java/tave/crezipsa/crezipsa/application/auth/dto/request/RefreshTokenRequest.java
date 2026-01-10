package tave.crezipsa.crezipsa.application.auth.dto.request;

public record RefreshTokenRequest(
        long userId,
        String refreshToken
) {
}
