package tave.crezipsa.crezipsa.application.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.global.common.TimeUtils;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

public record CommunityDetailResponse(
	Long communityId,
	String title,
	String content,
	CommunityField field,
	List<String> imageUrls,
	WriterResponse writer,
	boolean isWriter,
	boolean isLiked,
	long likeCount,
	long commentCount,
	String relativeTime,
	List<CommentResponse> comments
) {

	public static CommunityDetailResponse from(
		Community community,
		WriterResponse writer,
		boolean isWriter,
		boolean isLiked,
		long commentCount,
		List<CommentResponse> comments
	) {
		return new CommunityDetailResponse(
			community.getCommunityId(),
			community.getTitle(),
			community.getContent(),
			community.getField(),
			community.getImageUrls(),
			writer,
			isWriter,
			isLiked,
			community.getLikeCount(),
			commentCount,
			TimeUtils.convertToRelativeTime(community.getCreatedAt()),
			comments
		);
	}
}
