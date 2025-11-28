package tave.crezipsa.crezipsa.application.community.dto.response;

import static tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl.*;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record MyCommentResponse(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	String relativeTime  // "40분 전", "3시간 전", "1일 전"
) {

	public static MyCommentResponse of(Community community) {
		return new MyCommentResponse(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			preview(community.getContent()),
			convertToRelativeTime(community.getCreatedAt())
		);

	}
}
