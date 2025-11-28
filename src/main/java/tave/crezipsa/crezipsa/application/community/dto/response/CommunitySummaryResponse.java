package tave.crezipsa.crezipsa.application.community.dto.response;

import tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record CommunitySummaryResponse(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	long likeCount,
	long commentCount,
	String relativeTime,
	String thumbnailUrl
) {

	public static CommunitySummaryResponse of(
		Community community,
		long likeCount,
		long commentCount
	) {
		String thumbnail = (community.getImageUrls() != null && !community.getImageUrls().isEmpty())
			? community.getImageUrls().get(0)
			: null;

		return new CommunitySummaryResponse(
			community.getCommunityId(),
			community.getField(),
			community.getTitle(),
			LikeUseCaseImpl.preview(community.getContent()),
			likeCount,
			commentCount,
			LikeUseCaseImpl.convertToRelativeTime(community.getCreatedAt()),
			thumbnail
		);
	}
}
