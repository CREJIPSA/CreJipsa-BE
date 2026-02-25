package tave.crezipsa.crezipsa.application.community.dto.cache;

import java.time.LocalDateTime;
import java.util.List;

import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.application.community.usecase.LikeUseCaseImpl;

public record CommentCacheDto(
	Long commentId,
	Long communityId,
	Long parentId,
	Long userId,
	WriterResponse writer,
	boolean deleted,
	String content,
	LocalDateTime createdAt,
	List<CommentCacheDto> replies
) {
	public CommentResponse toResponse(Long viewerId) {
		boolean isWriter = viewerId != null && viewerId.equals(userId);
		List<CommentResponse> replyResponses = replies.stream()
			.map(r -> r.toResponse(viewerId))
			.toList();

		return new CommentResponse(
			commentId,
			communityId,
			parentId,
			writer,
			isWriter,
			deleted,
			content,
			createdAt,
			LikeUseCaseImpl.convertToRelativeTime(createdAt),
			replyResponses
		);
	}
}
