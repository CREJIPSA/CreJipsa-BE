package tave.crezipsa.crezipsa.application.community.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.application.community.mapper.CommentMapper;
import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@ExtendWith(MockitoExtension.class)
class CommentUseCaseImplTest {

	@Mock
	private CommentRepository commentRepository;
	@Mock
	private CommunityRepository communityRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CommentMapper commentMapper;

	@InjectMocks
	private CommentUseCaseImpl sut;

	@Nested
	@DisplayName("createComment")
	class CreateComment {

		@Test
		@DisplayName("대댓글의 대댓글 시도 시 INVALID_COMMENT_DEPTH 예외")
		void replyToReply_throwsInvalidDepth() {
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment parentReply = createComment(2L, communityId, 1L, 1L);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(2L)).thenReturn(Optional.of(parentReply));

			CommentCreateRequest request = new CommentCreateRequest(2L, "reply to reply");

			assertThatThrownBy(() -> sut.createComment(communityId, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_DEPTH);
		}

		@Test
		@DisplayName("다른 게시글의 댓글에 대댓글 시도 시 INVALID_PARENT_COMMUNITY 예외")
		void replyToDifferentCommunity_throwsInvalidParent() {
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment parentFromOtherPost = createComment(10L, 999L, 1L, null);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(10L)).thenReturn(Optional.of(parentFromOtherPost));

			CommentCreateRequest request = new CommentCreateRequest(10L, "cross-post reply");

			assertThatThrownBy(() -> sut.createComment(communityId, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_PARENT_COMMUNITY);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			when(communityRepository.findById(1L)).thenReturn(Optional.empty());

			CommentCreateRequest request = new CommentCreateRequest(null, "content");

			assertThatThrownBy(() -> sut.createComment(1L, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("deleteComment")
	class DeleteComment {

		@Test
		@DisplayName("루트 댓글은 소프트 삭제 (deleted=true, 내용 변경)")
		void rootComment_softDeletes() {
			Comment rootComment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(rootComment));

			sut.deleteComment(1L, 100L);

			assertThat(rootComment.isDeleted()).isTrue();
			assertThat(rootComment.getContent()).isEqualTo("삭제된 메시지입니다");
			verify(commentRepository, never()).delete(any());
		}

		@Test
		@DisplayName("자식 댓글은 하드 삭제 (DB에서 제거)")
		void childComment_hardDeletes() {
			Comment childComment = createComment(2L, 1L, 100L, 1L);
			when(commentRepository.findById(2L)).thenReturn(Optional.of(childComment));

			sut.deleteComment(2L, 100L);

			assertThat(childComment.isDeleted()).isFalse();
			verify(commentRepository).delete(childComment);
		}

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMENT 예외")
		void nonWriter_throwsUnauthorized() {
			Comment comment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			assertThatThrownBy(() -> sut.deleteComment(1L, 999L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMENT);
		}
	}

	@Nested
	@DisplayName("updateComment")
	class UpdateComment {

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMENT 예외")
		void nonWriter_throwsUnauthorized() {
			Comment comment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			assertThatThrownBy(() -> sut.updateComment(1L, 999L, new CommentUpdateRequest("new")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMENT);
		}
	}

	@Nested
	@DisplayName("getComments")
	class GetComments {

		@Test
		@DisplayName("루트 댓글과 자식 댓글을 트리 구조로 조합")
		void buildsNestedTree() {
			Long communityId = 1L;
			Long viewerId = 50L;
			Community community = createCommunity(communityId);

			Comment root1 = createComment(1L, communityId, 10L, null);
			Comment child1 = createComment(2L, communityId, 20L, 1L);
			Comment root2 = createComment(3L, communityId, 30L, null);

			User user10 = createUser(10L);
			User user20 = createUser(20L);
			User user30 = createUser(30L);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findByCommunityId(communityId)).thenReturn(List.of(root1, child1, root2));
			when(userRepository.findAllById(any())).thenReturn(List.of(user10, user20, user30));

			when(commentMapper.toCommentResponse(any(), any(), anyBoolean(), anyString(), anyList()))
				.thenAnswer(invocation -> {
					Comment c = invocation.getArgument(0);
					User w = invocation.getArgument(1);
					boolean isW = invocation.getArgument(2);
					String rt = invocation.getArgument(3);
					List<CommentResponse> replies = invocation.getArgument(4);
					return new CommentResponse(
						c.getCommentId(), c.getCommunityId(), c.getParentId(),
						WriterResponse.from(w), isW, c.isDeleted(), c.getContent(),
						c.getCreatedAt(), rt, replies
					);
				});

			List<CommentResponse> result = sut.getComments(communityId, viewerId);

			assertThat(result).hasSize(2);

			CommentResponse firstRoot = result.get(0);
			assertThat(firstRoot.commentId()).isEqualTo(1L);
			assertThat(firstRoot.replies()).hasSize(1);
			assertThat(firstRoot.replies().get(0).commentId()).isEqualTo(2L);

			CommentResponse secondRoot = result.get(1);
			assertThat(secondRoot.commentId()).isEqualTo(3L);
			assertThat(secondRoot.replies()).isEmpty();
		}

		@Test
		@DisplayName("빈 댓글 목록이면 빈 리스트 반환하고 User 조회 안 함")
		void emptyComments_returnsEmptyAndSkipsUserLookup() {
			Long communityId = 1L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findByCommunityId(communityId)).thenReturn(List.of());

			List<CommentResponse> result = sut.getComments(communityId, 1L);

			assertThat(result).isEmpty();
			verify(userRepository, never()).findAllById(any());
		}

		@Test
		@DisplayName("댓글 작성자의 writerMap에 없는 userId가 있으면 USER_NOT_FOUND 예외")
		void writerNotInMap_throwsUserNotFound() {
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment comment = createComment(1L, communityId, 999L, null);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findByCommunityId(communityId)).thenReturn(List.of(comment));
			when(userRepository.findAllById(any())).thenReturn(List.of());

			assertThatThrownBy(() -> sut.getComments(communityId, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.USER_NOT_FOUND);
		}
	}

	// ── 헬퍼 메서드 ──

	private Community createCommunity(Long id) {
		Community community = Community.builder()
			.communityId(id)
			.title("test")
			.content("content content content content content content content")
			.field(CommunityField.RECOMMEND)
			.imageUrls(List.of())
			.writerId(1L)
			.likeCount(0L)
			.build();
		ReflectionTestUtils.setField(community, "createdAt", LocalDateTime.now());
		return community;
	}

	private Comment createComment(Long commentId, Long communityId, Long userId, Long parentId) {
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

	private User createUser(Long userId) {
		return User.builder()
			.userId(userId)
			.nickName("user" + userId)
			.email("user" + userId + "@test.com")
			.profileImageUrl("profile.jpg")
			.mainPlatform(Platform.YOUTUBE)
			.activeYoutube("yt" + userId)
			.build();
	}
}
