package tave.crezipsa.crezipsa.application.community.dto.cache;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;

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
	LocalDateTime createdAt,
	List<CommentCacheDto> comments
) {
	public static CommunityDetailCacheDto from(
		Community community,
		WriterResponse writer,
		long commentCount,
		List<CommentCacheDto> comments
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
			community.getCreatedAt(),
			comments
		);
	}

	public CommunityDetailResponse toResponse(boolean isWriter, boolean isLiked, List<CommentResponse> comments) {
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
			LikeUseCaseImpl.convertToRelativeTime(createdAt),
			comments
		);
	}
}
