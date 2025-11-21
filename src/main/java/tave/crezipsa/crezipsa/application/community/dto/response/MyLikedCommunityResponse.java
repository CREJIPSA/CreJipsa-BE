package tave.crezipsa.crezipsa.application.community.dto.response;

import java.time.LocalDateTime;

import tave.crezipsa.crezipsa.domain.community.domain.Community;

public record MyLikedCommunityResponse(
	Long communityId,
	String title,
	String contentPreview,
	long likeCount,
	boolean liked,
	LocalDateTime createdAt
) {
	public static MyLikedCommunityResponse of(Community community, long likeCount) {
		return new MyLikedCommunityResponse(
			community.getCommunityId(),
			community.getTitle(),
			community.getContent().substring(0, Math.min(50, community.getContent().length())),
			likeCount,
			true,
			community.getCreatedAt()
		);
	}
}
