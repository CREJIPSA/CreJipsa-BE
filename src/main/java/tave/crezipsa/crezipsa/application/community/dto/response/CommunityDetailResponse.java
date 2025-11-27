package tave.crezipsa.crezipsa.application.community.dto.response;

import static tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl.*;

import java.time.LocalDateTime;
import java.util.List;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record CommunityDetailResponse(
	Long communityId,
	String title,
	String content,
	CommunityField field,
	List<String> imageUrls,
	Long writerId,
	long likeCount,
	long commentCount,
	String relativeTime,
	List<CommentResponse> comments
) {

	public static CommunityDetailResponse from(
		Community community,
		long commentCount,
		List<CommentResponse> comments
	) {
		return new CommunityDetailResponse(
			community.getCommunityId(),
			community.getTitle(),
			community.getContent(),
			community.getField(),
			community.getImageUrls(),
			community.getWriterId(),
			community.getLikeCount(),
			commentCount,
			convertToRelativeTime(community.getCreatedAt()),
			comments
		);
	}
}
