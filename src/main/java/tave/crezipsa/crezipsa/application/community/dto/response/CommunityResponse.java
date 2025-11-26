package tave.crezipsa.crezipsa.application.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record CommunityResponse(
	Long communityId,
	String title,
	String content,
	CommunityField field,
	List<String> imageUrls,
	Long writerId,
	Long likeCount,
	LocalDateTime createdAt
) {

	public static CommunityResponse from(Community community) {
		return new CommunityResponse(
			community.getCommunityId(),
			community.getTitle(),
			community.getContent(),
			community.getField(),
			community.getImageUrls(),
			community.getWriterId(),
			community.getLikeCount(),
			community.getCreatedAt()
		);
	}
}
