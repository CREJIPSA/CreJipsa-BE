package tave.crezipsa.crezipsa.fixture;

import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;

public final class CommentFixture {

	private CommentFixture() {
	}

	public static Comment createComment(Long commentId, Long communityId, Long userId, Long parentId) {
		Comment comment = Comment.builder()
			.commentId(commentId)
			.communityId(communityId)
			.userId(userId)
			.content("test comment")
			.parentId(parentId)
			.build();
		ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.now());
		return comment;
	}
}
