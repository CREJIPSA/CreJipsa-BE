package tave.crezipsa.crezipsa.application.community.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Component
@RequiredArgsConstructor
public class CommentMapper {

	private final UserRepository userRepository;

	public CommentResponse toCommentResponse(Comment comment) {

		User writer = userRepository.findById(comment.getContent())
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

		List<CommentResponse> replies = comment.getReplies().stream()
			.map(this::toCommentResponse)
			.toList();

		return new CommentResponse(
			comment.getCommentId(),
			comment.getCommunityId(),
			comment.getParentId(),
			writer.getUserId(),
			writer.getNickName(),
			comment.isDeleted(),
			comment.getContent(),
			comment.getCreatedAt(),
			replies
		);
	}
}
