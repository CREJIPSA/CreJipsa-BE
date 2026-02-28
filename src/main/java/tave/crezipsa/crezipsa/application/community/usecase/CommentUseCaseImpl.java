package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.community.cache.CommunityCacheService;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommentResponse;
import tave.crezipsa.crezipsa.application.community.mapper.CommentMapper;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.common.TimeUtils;
import tave.crezipsa.crezipsa.global.common.TransactionUtils;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentUseCaseImpl implements CommentUsecase {

	private final CommentRepository commentRepository;
	private final CommunityRepository communityRepository;
	private final UserRepository userRepository;
	private final CommentMapper commentMapper;
	private final CommunityCacheService communityCacheService;

	@Override
	public CommentResponse createComment(Long communityId, Long userId, CommentCreateRequest request) {
		findCommunityOrThrow(communityId);
		Long parentId = request.parentId();

		if (parentId != null) {
			Comment parent = findCommentOrThrow(parentId);
			validateParentComment(parent);
			validateSameCommunity(parent, communityId);
		}

		Comment saved = commentRepository.save(Comment.create(communityId, userId, request.content(), parentId));
		User writer = findUserOrThrow(userId);
		String relativeTime = TimeUtils.convertToRelativeTime(saved.getCreatedAt());

		TransactionUtils.afterCommit(() -> communityCacheService.evictCommentsAndDetail(communityId));
		return commentMapper.toCommentResponse(saved, writer, true, relativeTime, List.of());
	}

	@Override
	public CommentResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {
		Comment comment = findCommentOrThrow(commentId);

		if (!comment.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		comment.update(request.content());
		User writer = findUserOrThrow(userId);
		String relativeTime = TimeUtils.convertToRelativeTime(comment.getCreatedAt());

		communityCacheService.evictCommentsAndDetail(comment.getCommunityId());
		return commentMapper.toCommentResponse(comment, writer, true, relativeTime, List.of());
	}

	@Override
	public void deleteComment(Long commentId, Long userId) {
		Comment comment = findCommentOrThrow(commentId);

		if (!comment.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		Long communityId = comment.getCommunityId();

		if (comment.getParentId() == null) {
			comment.softDelete();
		} else {
			commentRepository.delete(comment);
		}

		TransactionUtils.afterCommit(() -> communityCacheService.evictCommentsAndDetail(communityId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CommentResponse> getComments(Long communityId, Long userId) {
		findCommunityOrThrow(communityId);

		return communityCacheService.getCommentsCache(communityId).stream()
			.map(dto -> dto.toResponse(userId))
			.toList();
	}

	@Override
	public List<MyCommentResponse> getMyComments(Long userId, CommunityField field, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		return commentRepository
			.findMyCommentsByCommunityField(userId, field, pageable)
			.stream()
			.map(MyCommentResponse::of)
			.toList();
	}

	private User findUserOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
	}

	private Comment findCommentOrThrow(Long commentId) {
		return commentRepository.findById(commentId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMENT_NOT_FOUND));
	}

	private Community findCommunityOrThrow(Long communityId) {
		return communityRepository.findById(communityId)
			.orElseThrow(() -> new CommonException(ErrorCode.COMMUNITY_NOT_FOUND));
	}

	private void validateParentComment(Comment parent) {
		if (parent.getParentId() != null) {
			throw new CommonException(ErrorCode.INVALID_COMMENT_DEPTH);
		}
	}

	private void validateSameCommunity(Comment parent, Long communityId) {
		if (!parent.getCommunityId().equals(communityId)) {
			throw new CommonException(ErrorCode.INVALID_PARENT_COMMUNITY);
		}
	}
}
