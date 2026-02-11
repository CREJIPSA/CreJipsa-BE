package tave.crezipsa.crezipsa.fixture;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public final class UserFixture {

	private UserFixture() {
	}

	public static User createUser(Long userId) {
		return User.builder()
			.userId(userId)
			.nickName("user" + userId)
			.email("user" + userId + "@test.com")
			.profileImageUrl("profile.jpg")
			.mainPlatform(Platform.YOUTUBE)
			.activeYoutube("yt" + userId)
			.build();
	}
}
