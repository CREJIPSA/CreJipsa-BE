package tave.crezipsa.crezipsa.application.community.dto.response;

import static tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl.*;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record MyLikedCommunityResponse(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	long likeCount,
	long commentCount,
	String relativeTime  // "40분 전", "3시간 전", "1일 전"
) {

	public static MyLikedCommunityResponse of(
		Community community,
		long likeCount,
		long commentCount
	) {
		return new MyLikedCommunityResponse(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			preview(community.getContent()),
			likeCount,
			commentCount,
			convertToRelativeTime(community.getCreatedAt())
		);
	}


}
