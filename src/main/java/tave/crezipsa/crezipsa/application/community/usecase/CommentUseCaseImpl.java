package tave.crezipsa.crezipsa.application.community.usecase;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class CommentUseCaseImpl implements  CommentUsecase {

	private final CommentRepository commentRepository;
	private final CommunityRepository communityRepository;
	private final UserRepository userRepository;
	private final CommentMapper commentMapper;

	@Override
	public CommentResponse createComment(Long communityId, Long userId, CommentCreateRequest request) {

		findCommunityOrThrow(communityId);
		Long parentId = request.parentId();

		if (parentId != null) {
			Comment parent = findCommentOrThrow(parentId);
			validateParentComment(parent);
		}

		Comment saved = commentRepository.save(Comment.create(communityId, userId, request.content(), parentId));
		User writer = findUserOrThrow(userId);

		return commentMapper.toCommentResponse(saved, writer, List.of());
	}

	@Override
	public CommentResponse updateComment(Long commentId, Long userId, CommentUpdateRequest request) {

		Comment comment = findCommentOrThrow(commentId);

		if (!comment.getUserId().equals(userId)) {
			throw new CommonException(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		comment.update(request.content());
		User writer = findUserOrThrow(userId);

		return commentMapper.toCommentResponse(comment, writer, List.of());
	}

	@Override
	public void deleteComment(Long commentId, Long userId) {

		Comment comment = findCommentOrThrow(commentId);

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
		findCommunityOrThrow(communityId);

		List<Comment> allComments = commentRepository.findByCommunityId(communityId);
		if (allComments.isEmpty()) {
			return List.of();
		}

		Set<Long> writerIds = allComments.stream()
			.map(Comment::getUserId)
			.collect(Collectors.toSet());

		List<User> writers = userRepository.findAllById(writerIds);

		Map<Long, User> writerMap = writers.stream()
			.collect(Collectors.toMap(User::getUserId, u -> u));

		Map<Long, List<Comment>> childrenByParentId = allComments.stream()
			.filter(c -> c.getParentId() != null)
			.collect(Collectors.groupingBy(Comment::getParentId));

		List<Comment> rootComments = allComments.stream()
			.filter(c -> c.getParentId() == null)
			.sorted(Comparator.comparing(Comment::getCreatedAt))
			.toList();

		return rootComments.stream()
			.map(root -> toResponseTree(root, childrenByParentId, writerMap))
			.toList();

	}

	@Override
	public List<CommentResponse> getMyComments(Long userId) {

		List<Comment> myComments = commentRepository.findByUserId(userId);

		return myComments.stream()
			.map(comment -> {
				User writer = findUserOrThrow(comment.getUserId());

				return new CommentResponse(
					comment.getCommentId(),
					comment.getCommunityId(),
					comment.getParentId(),
					comment.getUserId(),
					writer.getNickName(),
					writer.getProfileImageUrl(),
					comment.isDeleted(),
					comment.getContent(),
					comment.getCreatedAt(),
					List.of()
				);
			})
			.toList();
	}

	private CommentResponse toResponseTree(Comment comment, Map<Long, List<Comment>> childrenByParentId,
		Map<Long, User> writerMap) {
		User writer = writerMap.get(comment.getUserId());
		if (writer == null) {
			throw new CommonException(ErrorCode.USER_NOT_FOUND);
		}

		// 나를 부모로 가진 자식 댓글들
		List<Comment> children = childrenByParentId.getOrDefault(comment.getCommentId(), List.of());

		// 자식들도 재귀적으로 CommentResponse로 변환
		List<CommentResponse> replyResponses = children.stream()
			.sorted(Comparator.comparing(Comment::getCreatedAt)) // 대댓글도 정렬(선택)
			.map(child -> toResponseTree(child, childrenByParentId, writerMap))
			.toList();

		// Mapper는 변환만
		return commentMapper.toCommentResponse(comment, writer, replyResponses);
	}

	// 검증 로직의 반복이 잦아 헬퍼 메소드로 분리
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
}
