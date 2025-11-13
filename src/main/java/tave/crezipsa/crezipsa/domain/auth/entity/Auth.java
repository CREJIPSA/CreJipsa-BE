package tave.crezipsa.crezipsa.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Auth {
    private Long authId;
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String providerUserId;
}
