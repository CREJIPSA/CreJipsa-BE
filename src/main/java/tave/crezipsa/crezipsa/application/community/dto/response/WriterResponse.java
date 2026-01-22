package tave.crezipsa.crezipsa.application.community.dto.response;

import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;

public record WriterResponse(
	Long userId,
	String nickName,
	String profileImageUrl,
	Platform mainPlatform,
	String mainPlatformId
) {
	public static WriterResponse from(User user) {
		return new WriterResponse(
			user.getUserId(),
			user.getNickName(),
			user.getProfileImageUrl(),
			user.getMainPlatform(),
			user.getMainPlatformAccount()
		);
}

}
