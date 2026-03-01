package tave.crezipsa.crezipsa.application.community.dto.cache;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.global.common.TimeUtils;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
public record CommunityDetailCacheDto(
	Long communityId,
	Long writerId,
	String title,
	String content,
	CommunityField field,
	List<String> imageUrls,
	WriterResponse writer,
	long likeCount,
	long commentCount,
	LocalDateTime createdAt
) {
	public static CommunityDetailCacheDto from(
		Community community,
		WriterResponse writer,
		long commentCount
	) {
		return new CommunityDetailCacheDto(
			community.getCommunityId(),
			community.getWriterId(),
			community.getTitle(),
			community.getContent(),
			community.getField(),
			community.getImageUrls(),
			writer,
			community.getLikeCount(),
			commentCount,
			community.getCreatedAt()
		);
	}

	public CommunityDetailResponse toResponse(boolean isWriter, boolean isLiked, long likeCount, List<CommentResponse> comments) {
		return new CommunityDetailResponse(
			communityId,
			title,
			content,
			field,
			imageUrls,
			writer,
			isWriter,
			isLiked,
			likeCount,
			commentCount,
			TimeUtils.convertToRelativeTime(createdAt),
			comments
		);
	}
}
