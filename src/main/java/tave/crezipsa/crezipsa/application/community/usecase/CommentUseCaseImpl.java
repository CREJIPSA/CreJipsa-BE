package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.mapper.CommentMapper;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
public class CommentUseCaseImpl implements  CommentUsecase{

	private final CommentRepository commentRepository;
	private final CommunityRepository communityRepository;
	private final UserRepository userRepository;
	private final CommentMapper commentMapper;

	@Override
	public CommentResponse createComment(Long communityId, Long userId, CommentCreateRequest request) {

		Community community = communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		Long parentId = request.parentId();

		if (parentId != null) {
			Comment parent = commentRepository.findById(parentId)
				.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));

			if (parent.getParentId() != null) {
				throw new CommonException(ErrorCode.INVALID_COMMENT_DEPTH);
			}
		}

		Comment comment = Comment.create(communityId, userId, request.content(), parentId);
		Comment saved = commentRepository.save(comment);

		return commentMapper.toCommentResponse(saved);

	}

	@Override
	public CommentResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));

		if (!comment.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		comment.update(request.content());
		return commentMapper.toCommentResponse(comment);
	}

	@Override
	public void deleteComment(Long commentId, Long userId) {

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));

		if (!comment.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		if (comment.getParentId() == null) {
			comment.softDelete();
		} else {
			commentRepository.delete(comment);
		}
	}

	@Override
	public List<CommentResponse> getComments(Long communityId) {
		communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

		List<Comment> allComments = commentRepository.findByCommunityId(communityId);

		return allComments.stream()
			.filter(comment -> comment.getParentId() == null)
			.map(commentMapper::toCommentResponse)
			.toList();

	}

	@Override
	public List<CommentResponse> getMyComments(Long userId) {

		List<Comment> myComments = commentRepository.findByUserId(userId);

		return myComments.stream()
			.map(comment -> {
				Community community = communityRepository.findById(comment.getCommunityId())
					.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));

				User user = userRepository.findById(comment.getUserId())
					.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

				return new CommentResponse(
					comment.getCommentId(),
					comment.getCommunityId(),
					comment.getParentId(),
					comment.getUserId(),
					user.getNickName(),
					user.getProfileImageUrl(),
					comment.isDeleted(),
					comment.getContent(),
					comment.getCreatedAt(),
					List.of()
				);
			})
			.toList();
	}

}
