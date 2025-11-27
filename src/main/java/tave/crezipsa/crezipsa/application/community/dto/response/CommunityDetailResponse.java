package tave.crezipsa.crezipsa.application.community.dto.response;

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

	// 작성자 정보
	Long writerId,
	String writerNickname,
	String writerProfileImage,

	// 상태 정보
	boolean isLikedByMe,
	boolean isMine,

	long likeCount,
	long commentCount,

	String relativeTime,

	// 댓글 목록 전체
	List<CommentResponse> comments
) {

	public static CommunityDetailResponse from(Community community ,long commentCount) {
		return new CommunityDetailResponse(
			community.getCommunityId(),
			community.getTitle(),
			community.getContent(),
			community.getField(),
			community.getImageUrls(),
			community.getWriterId(),
			community.getLikeCount(),
			commentCount,
			community.getCreatedAt()
		);
	}
}
