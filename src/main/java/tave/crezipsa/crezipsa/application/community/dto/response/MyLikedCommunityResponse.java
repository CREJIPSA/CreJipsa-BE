package tave.crezipsa.crezipsa.application.community.dto.response;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.global.common.TimeUtils;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record MyLikedCommunityResponse(
	Long communityId,
	CommunityField field,
	String title,
	String contentPreview,
	long likeCount,
	long commentCount,
	String relativeTime,  // "40분 전", "3시간 전", "1일 전"
	String thumbnailUrl
) {

	public static MyLikedCommunityResponse of(
		Community community,
		long likeCount,
		long commentCount
	) {
		String thumbnail = (community.getImageUrls() != null && !community.getImageUrls().isEmpty())
			? community.getImageUrls().get(0)
			: null;

		return new MyLikedCommunityResponse(
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
