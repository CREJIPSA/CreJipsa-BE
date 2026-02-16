package tave.crezipsa.crezipsa.domain.community.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

class CommunityDomainTest {

	@Nested
	@DisplayName("Community.create")
	class Create {

		@Test
		@DisplayName("TIP 필드에 이미지 없으면 INVALID_TIP_UPLOAD 예외")
		void tipFieldWithoutImages_throws() {
			// when & then
			assertThatThrownBy(() ->
				Community.create("title", "content", CommunityField.TIP, null, 1L)
			)
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_TIP_UPLOAD);
		}

		@Test
		@DisplayName("TIP 필드에 빈 이미지 목록이면 INVALID_TIP_UPLOAD 예외")
		void tipFieldWithEmptyImages_throws() {
			// when & then
			assertThatThrownBy(() ->
				Community.create("title", "content", CommunityField.TIP, List.of(), 1L)
			)
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_TIP_UPLOAD);
		}

		@Test
		@DisplayName("TIP 필드에 이미지 있으면 정상 생성")
		void tipFieldWithImages_succeeds() {
			// when
			Community community = Community.create("title", "content", CommunityField.TIP, List.of("img.jpg"), 1L);

			// then
			assertThat(community.getField()).isEqualTo(CommunityField.TIP);
			assertThat(community.getImageUrls()).containsExactly("img.jpg");
			assertThat(community.getLikeCount()).isZero();
		}

		@Test
		@DisplayName("null 필드면 INVALID_FIELD_TYPE 예외")
		void nullField_throwsInvalidFieldType() {
			// when & then
			assertThatThrownBy(() ->
				Community.create("title", "content", null, null, 1L)
			)
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_FIELD_TYPE);
		}

		@Test
		@DisplayName("RECOMMEND 필드는 이미지 없이 생성 가능")
		void recommendFieldWithoutImages_succeeds() {
			// when
			Community community = Community.create("title", "content", CommunityField.RECOMMEND, null, 1L);

			// then
			assertThat(community.getImageUrls()).isEmpty();
			assertThat(community.getLikeCount()).isZero();
			assertThat(community.getWriterId()).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("Community 좋아요 카운트")
	class LikeCount {

		@Test
		@DisplayName("좋아요 증가")
		void increase() {
			// given
			Community community = Community.create("t", "c", CommunityField.RECOMMEND, null, 1L);

			// when
			community.increaseLikeCount();
			community.increaseLikeCount();

			// then
			assertThat(community.getLikeCount()).isEqualTo(2L);
		}

		@Test
		@DisplayName("좋아요 감소 시 카운트가 1 이상이면 정상 감소")
		void decrease_whenPositive() {
			// given
			Community community = Community.create("t", "c", CommunityField.RECOMMEND, null, 1L);
			community.increaseLikeCount();
			community.increaseLikeCount();

			// when
			community.decreaseLikeCount();

			// then
			assertThat(community.getLikeCount()).isEqualTo(1L);
		}

		@Test
		@DisplayName("좋아요 감소 시 0 이하로 내려가지 않음")
		void decreaseFloorAtZero() {
			// given
			Community community = Community.create("t", "c", CommunityField.RECOMMEND, null, 1L);

			// when
			community.decreaseLikeCount();

			// then
			assertThat(community.getLikeCount()).isZero();
		}
	}

	@Nested
	@DisplayName("Community.update")
	class Update {

		@Test
		@DisplayName("null 필드는 업데이트하지 않음 (선택적 업데이트)")
		void selectiveUpdate() {
			// given
			Community community = Community.create("title", "content", CommunityField.RECOMMEND, null, 1L);

			// when
			community.update("new title", null, null);

			// then
			assertThat(community.getTitle()).isEqualTo("new title");
			assertThat(community.getContent()).isEqualTo("content");
		}

		@Test
		@DisplayName("모든 필드를 한번에 업데이트")
		void fullUpdate() {
			// given
			Community community = Community.create("title", "content", CommunityField.RECOMMEND, null, 1L);

			// when
			community.update("new title", "new content", List.of("new.jpg"));

			// then
			assertThat(community.getTitle()).isEqualTo("new title");
			assertThat(community.getContent()).isEqualTo("new content");
			assertThat(community.getImageUrls()).containsExactly("new.jpg");
		}

		@Test
		@DisplayName("TIP 필드 커뮤니티에서 이미지를 빈 목록으로 업데이트하면 예외")
		void updateTipCommunityWithEmptyImages_throws() {
			// given
			Community community = Community.create("title", "content", CommunityField.TIP, List.of("img.jpg"), 1L);

			// when & then
			assertThatThrownBy(() -> community.update(null, null, List.of()))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_TIP_UPLOAD);
		}
	}

	@Nested
	@DisplayName("Comment 도메인")
	class CommentDomain {

		@Test
		@DisplayName("정상 내용으로 생성하면 Comment 객체 반환")
		void createWithValidContent_succeeds() {
			// when
			Comment comment = Comment.create(1L, 2L, "정상 댓글", null);

			// then
			assertThat(comment.getCommunityId()).isEqualTo(1L);
			assertThat(comment.getUserId()).isEqualTo(2L);
			assertThat(comment.getContent()).isEqualTo("정상 댓글");
			assertThat(comment.getParentId()).isNull();
		}

		@Test
		@DisplayName("대댓글 생성 시 parentId가 설정됨")
		void createReply_setsParentId() {
			// when
			Comment reply = Comment.create(1L, 2L, "대댓글", 10L);

			// then
			assertThat(reply.getParentId()).isEqualTo(10L);
		}

		@Test
		@DisplayName("null 내용으로 생성하면 INVALID_COMMENT_CONTENT 예외")
		void createWithNullContent_throws() {
			// when & then
			assertThatThrownBy(() -> Comment.create(1L, 1L, null, null))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_CONTENT);
		}

		@Test
		@DisplayName("빈 내용으로 생성하면 INVALID_COMMENT_CONTENT 예외")
		void createWithEmptyContent_throws() {
			// when & then
			assertThatThrownBy(() -> Comment.create(1L, 1L, "", null))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_CONTENT);
		}

		@Test
		@DisplayName("softDelete 시 deleted=true, 내용이 삭제 메시지로 변경")
		void softDelete_setsDeletedAndChangesContent() {
			// given
			Comment comment = Comment.create(1L, 1L, "original content", null);

			// when
			comment.softDelete();

			// then
			assertThat(comment.isDeleted()).isTrue();
			assertThat(comment.getContent()).isEqualTo("삭제된 메시지입니다");
		}

		@Test
		@DisplayName("update 시 정상 내용이면 내용 변경")
		void updateWithValidContent_succeeds() {
			// given
			Comment comment = Comment.create(1L, 1L, "original", null);

			// when
			comment.update("수정된 내용");

			// then
			assertThat(comment.getContent()).isEqualTo("수정된 내용");
		}

		@Test
		@DisplayName("update 시 null 내용이면 INVALID_COMMENT_CONTENT 예외")
		void updateWithNullContent_throws() {
			// given
			Comment comment = Comment.create(1L, 1L, "original", null);

			// when & then
			assertThatThrownBy(() -> comment.update(null))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_CONTENT);
		}

		@Test
		@DisplayName("update 시 빈 내용이면 INVALID_COMMENT_CONTENT 예외")
		void updateWithEmptyContent_throws() {
			// given
			Comment comment = Comment.create(1L, 1L, "original", null);

			// when & then
			assertThatThrownBy(() -> comment.update(""))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_COMMENT_CONTENT);
		}
	}

	@Nested
	@DisplayName("Like 도메인")
	class LikeDomain {

		@Test
		@DisplayName("Like.of 생성 시 isLiked=true")
		void of_createsWithIsLikedTrue() {
			// when
			Like like = Like.of(1L, 10L);

			// then
			assertThat(like.isLiked()).isTrue();
			assertThat(like.getUserId()).isEqualTo(1L);
			assertThat(like.getCommunityId()).isEqualTo(10L);
		}

		@Test
		@DisplayName("unlike 후 like하면 isLiked 복원")
		void toggleLikeUnlike() {
			// given
			Like like = Like.of(1L, 10L);

			// when & then
			like.unlike();
			assertThat(like.isLiked()).isFalse();

			like.like();
			assertThat(like.isLiked()).isTrue();
		}
	}
}
