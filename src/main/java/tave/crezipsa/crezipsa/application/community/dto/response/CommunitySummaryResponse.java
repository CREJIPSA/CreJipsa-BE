package tave.crezipsa.crezipsa.application.community.dto.response;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.global.common.TimeUtils;
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
			TimeUtils.preview(community.getContent()),
			likeCount,
			commentCount,
			TimeUtils.convertToRelativeTime(community.getCreatedAt()),
			thumbnail
		);
	}
}
