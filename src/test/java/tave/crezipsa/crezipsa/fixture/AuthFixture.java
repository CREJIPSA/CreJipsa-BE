package tave.crezipsa.crezipsa.fixture;

import tave.crezipsa.crezipsa.domain.auth.entity.Auth;

public final class AuthFixture {

	private AuthFixture() {
	}

	public static Auth createAuth(Long userId) {
		return Auth.builder()
			.authId(1L)
			.userId(userId)
			.accessToken("access-token-" + userId)
			.refreshToken("refresh-token-" + userId)
			.providerUserId("kakao-" + userId)
			.build();
	}

	public static Auth createAuth(Long userId, String accessToken, String refreshToken) {
		return Auth.builder()
			.authId(1L)
			.userId(userId)
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.providerUserId("kakao-" + userId)
			.build();
	}
}
