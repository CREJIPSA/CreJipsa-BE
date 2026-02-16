package tave.crezipsa.crezipsa.application.community.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.CommentFixture.*;
import static tave.crezipsa.crezipsa.fixture.CommunityFixture.*;
import static tave.crezipsa.crezipsa.fixture.UserFixture.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tave.crezipsa.crezipsa.application.community.dto.request.CommentCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommentUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommentResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.WriterResponse;
import tave.crezipsa.crezipsa.application.community.mapper.CommentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import tave.crezipsa.crezipsa.domain.community.domain.Comment;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
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
		@DisplayName("루트 댓글 정상 생성")
		void rootComment_success() {
			// given
			Long communityId = 1L;
			Long userId = 10L;
			Community community = createCommunity(communityId);
			Comment saved = createComment(100L, communityId, userId, null);
			User writer = createUser(userId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.save(any(Comment.class))).thenReturn(saved);
			when(userRepository.findById(userId)).thenReturn(Optional.of(writer));
			when(commentMapper.toCommentResponse(any(), any(), anyBoolean(), anyString(), anyList()))
				.thenReturn(new CommentResponse(100L, communityId, null, WriterResponse.from(writer), true, false, "test comment", null, "방금 전", List.of()));

			CommentCreateRequest request = new CommentCreateRequest(null, "test comment");

			// when
			CommentResponse result = sut.createComment(communityId, userId, request);

			// then
			assertThat(result.commentId()).isEqualTo(100L);
			assertThat(result.parentId()).isNull();
			verify(commentRepository).save(any(Comment.class));
		}

		@Test
		@DisplayName("대댓글 정상 생성 (루트 댓글에 답글)")
		void replyToRoot_success() {
			// given
			Long communityId = 1L;
			Long userId = 10L;
			Comment parentRoot = createComment(1L, communityId, 5L, null);
			Comment saved = createComment(100L, communityId, userId, 1L);
			Community community = createCommunity(communityId);
			User writer = createUser(userId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(1L)).thenReturn(Optional.of(parentRoot));
			when(commentRepository.save(any(Comment.class))).thenReturn(saved);
			when(userRepository.findById(userId)).thenReturn(Optional.of(writer));
			when(commentMapper.toCommentResponse(any(), any(), anyBoolean(), anyString(), anyList()))
				.thenReturn(new CommentResponse(100L, communityId, 1L, WriterResponse.from(writer), true, false, "reply", null, "방금 전", List.of()));

			CommentCreateRequest request = new CommentCreateRequest(1L, "reply");

			// when
			CommentResponse result = sut.createComment(communityId, userId, request);

			// then
			assertThat(result.commentId()).isEqualTo(100L);
			assertThat(result.parentId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("부모 댓글이 존재하지 않으면 COMMENT_NOT_FOUND 예외")
		void parentNotFound_throws() {
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(999L)).thenReturn(Optional.empty());

			CommentCreateRequest request = new CommentCreateRequest(999L, "content");

			// when & then
			assertThatThrownBy(() -> sut.createComment(communityId, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
		}

		@Test
		@DisplayName("대댓글의 대댓글 시도 시 INVALID_COMMENT_DEPTH 예외")
		void replyToReply_throwsInvalidDepth() {
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment parentReply = createComment(2L, communityId, 1L, 1L);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(2L)).thenReturn(Optional.of(parentReply));

			CommentCreateRequest request = new CommentCreateRequest(2L, "reply to reply");

			// when & then
			assertThatThrownBy(() -> sut.createComment(communityId, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_DEPTH);
		}

		@Test
		@DisplayName("다른 게시글의 댓글에 대댓글 시도 시 INVALID_PARENT_COMMUNITY 예외")
		void replyToDifferentCommunity_throwsInvalidParent() {
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment parentFromOtherPost = createComment(10L, 999L, 1L, null);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findById(10L)).thenReturn(Optional.of(parentFromOtherPost));

			CommentCreateRequest request = new CommentCreateRequest(10L, "cross-post reply");

			// when & then
			assertThatThrownBy(() -> sut.createComment(communityId, 1L, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_PARENT_COMMUNITY);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			// given
			when(communityRepository.findById(1L)).thenReturn(Optional.empty());
			CommentCreateRequest request = new CommentCreateRequest(null, "content");

			// when & then
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
			// given
			Comment rootComment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(rootComment));

			// when
			sut.deleteComment(1L, 100L);

			// then
			assertThat(rootComment.isDeleted()).isTrue();
			assertThat(rootComment.getContent()).isEqualTo("삭제된 메시지입니다");
			verify(commentRepository, never()).delete(any());
		}

		@Test
		@DisplayName("자식 댓글은 하드 삭제 (DB에서 제거)")
		void childComment_hardDeletes() {
			// given
			Comment childComment = createComment(2L, 1L, 100L, 1L);
			when(commentRepository.findById(2L)).thenReturn(Optional.of(childComment));

			// when
			sut.deleteComment(2L, 100L);

			// then
			assertThat(childComment.isDeleted()).isFalse();
			verify(commentRepository).delete(childComment);
		}

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMENT 예외")
		void nonWriter_throwsUnauthorized() {
			// given
			Comment comment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// when & then
			assertThatThrownBy(() -> sut.deleteComment(1L, 999L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		@Test
		@DisplayName("존재하지 않는 댓글이면 COMMENT_NOT_FOUND 예외")
		void commentNotFound_throws() {
			// given
			when(commentRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.deleteComment(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("updateComment")
	class UpdateComment {

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMENT 예외")
		void nonWriter_throwsUnauthorized() {
			// given
			Comment comment = createComment(1L, 1L, 100L, null);
			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

			// when & then
			assertThatThrownBy(() -> sut.updateComment(1L, 999L, new CommentUpdateRequest("new")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMENT);
		}

		@Test
		@DisplayName("존재하지 않는 댓글이면 COMMENT_NOT_FOUND 예외")
		void commentNotFound_throws() {
			// given
			when(commentRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.updateComment(999L, 1L, new CommentUpdateRequest("new")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMENT_NOT_FOUND);
		}

		@Test
		@DisplayName("작성자가 맞으면 댓글 정상 수정")
		void writer_updatesSuccessfully() {
			// given
			Comment comment = createComment(1L, 1L, 100L, null);
			User writer = createUser(100L);

			when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
			when(userRepository.findById(100L)).thenReturn(Optional.of(writer));
			when(commentMapper.toCommentResponse(any(), any(), anyBoolean(), anyString(), anyList()))
				.thenReturn(new CommentResponse(1L, 1L, null, WriterResponse.from(writer), true, false, "updated", null, "방금 전", List.of()));

			// when
			CommentResponse result = sut.updateComment(1L, 100L, new CommentUpdateRequest("updated"));

			// then
			assertThat(result.content()).isEqualTo("updated");
		}
	}

	@Nested
	@DisplayName("getComments")
	class GetComments {

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			// given
			when(communityRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.getComments(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}

		@Test
		@DisplayName("루트 댓글과 자식 댓글을 트리 구조로 조합")
		void buildsNestedTree() {
			// given
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

			// when
			List<CommentResponse> result = sut.getComments(communityId, viewerId);

			// then
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
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findByCommunityId(communityId)).thenReturn(List.of());

			// when
			List<CommentResponse> result = sut.getComments(communityId, 1L);

			// then
			assertThat(result).isEmpty();
			verify(userRepository, never()).findAllById(any());
		}

		@Test
		@DisplayName("댓글 작성자의 writerMap에 없는 userId가 있으면 USER_NOT_FOUND 예외")
		void writerNotInMap_throwsUserNotFound() {
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId);
			Comment comment = createComment(1L, communityId, 999L, null);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(commentRepository.findByCommunityId(communityId)).thenReturn(List.of(comment));
			when(userRepository.findAllById(any())).thenReturn(List.of());

			// when & then
			assertThatThrownBy(() -> sut.getComments(communityId, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.USER_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("getMyComments")
	class GetMyComments {

		@Test
		@DisplayName("내가 댓글 단 게시글 목록 조회")
		void returnsMyCommentedCommunities() {
			// given
			Long userId = 10L;
			Community c1 = createCommunity(1L, 5L);
			Page<Community> page = new PageImpl<>(List.of(c1), PageRequest.of(0, 10), 1);

			when(commentRepository.findMyCommentsByCommunityField(eq(userId), eq(CommunityField.RECOMMEND), any()))
				.thenReturn(page);

			// when
			List<MyCommentResponse> result = sut.getMyComments(userId, CommunityField.RECOMMEND, 0, 10);

			// then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).communityId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("댓글 없으면 빈 리스트 반환")
		void noComments_returnsEmpty() {
			// given
			Page<Community> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

			when(commentRepository.findMyCommentsByCommunityField(eq(10L), isNull(), any()))
				.thenReturn(emptyPage);

			// when
			List<MyCommentResponse> result = sut.getMyComments(10L, null, 0, 10);

			// then
			assertThat(result).isEmpty();
		}
	}
}
