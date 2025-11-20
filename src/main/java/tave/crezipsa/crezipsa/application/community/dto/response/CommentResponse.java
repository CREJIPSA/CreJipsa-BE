package tave.crezipsa.crezipsa.application.community.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
	Long commentId,
	Long communityId,
	Long parentId,
	Long writerId,
	String writerNickname,
	String profileImageUrl,
	boolean deleted,
	String content,
	LocalDateTime createdAt,
	List<CommentResponse> replies
) {
}
