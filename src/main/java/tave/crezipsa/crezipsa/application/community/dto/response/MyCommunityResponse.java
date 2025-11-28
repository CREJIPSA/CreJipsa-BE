package tave.crezipsa.crezipsa.application.community.dto.response;

import static tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl.*;

import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Community;

public record MyCommunityResponse(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	long likeCount,
	long commentCount,
	String relativeTime,
	String thumbnailUrl
) {

	public static MyCommunityResponse of(
		Community community,
		long likeCount,
		long commentCount
	) {
		String thumbnail = (community.getImageUrls() != null && !community.getImageUrls().isEmpty())
			? community.getImageUrls().get(0)
			: null;

		return new MyCommunityResponse(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			preview(community.getContent()),
			likeCount,
			commentCount,
			convertToRelativeTime(community.getCreatedAt()),
			thumbnail
		);
	}
}
