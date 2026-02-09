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

import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@ExtendWith(MockitoExtension.class)
class LikeUseCaseImplTest {

	@Mock
	private LikeRepository likeRepository;
	@Mock
	private CommunityRepository communityRepository;
	@Mock
	private CommentRepository commentRepository;

	@InjectMocks
	private LikeUseCaseImpl sut;

	@Nested
	@DisplayName("like")
	class LikeAction {

		@Test
		@DisplayName("새로운 좋아요 - Like 생성 후 커뮤니티 likeCount 증가")
		void newLike_createsAndIncrements() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.empty());

			sut.like(userId, communityId);

			verify(likeRepository).save(any(Like.class));
			assertThat(community.getLikeCount()).isEqualTo(1L);
		}

		@Test
		@DisplayName("이미 좋아요 상태 - unlike으로 토글, likeCount 감소")
		void existingLiked_togglesOff() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);
			community.increaseLikeCount();

			Like existingLike = Like.of(userId, communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			sut.like(userId, communityId);

			assertThat(existingLike.isLiked()).isFalse();
			assertThat(community.getLikeCount()).isZero();
			verify(likeRepository, never()).save(any());
		}

		@Test
		@DisplayName("좋아요 취소 상태에서 다시 좋아요 - like으로 토글, likeCount 증가")
		void existingUnliked_togglesOn() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			Like existingLike = Like.of(userId, communityId);
			existingLike.unlike();

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			sut.like(userId, communityId);

			assertThat(existingLike.isLiked()).isTrue();
			assertThat(community.getLikeCount()).isEqualTo(1L);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			when(communityRepository.findById(999L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> sut.like(1L, 999L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}
	}

	@Nested
	@DisplayName("unlike")
	class UnlikeAction {

		@Test
		@DisplayName("좋아요 기록이 없으면 NOT_LIKED 예외")
		void noLikeRecord_throwsNotLiked() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.empty());

			assertThatThrownBy(() -> sut.unlike(userId, communityId))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.NOT_LIKED);
		}

		@Test
		@DisplayName("이미 unlike 상태면 NOT_LIKED 예외")
		void alreadyUnliked_throwsNotLiked() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			Like existingLike = Like.of(userId, communityId);
			existingLike.unlike();

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			assertThatThrownBy(() -> sut.unlike(userId, communityId))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.NOT_LIKED);
		}

		@Test
		@DisplayName("정상 unlike - isLiked=false로 변경, likeCount 감소")
		void liked_decrementsCount() {
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);
			community.increaseLikeCount();

			Like existingLike = Like.of(userId, communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			sut.unlike(userId, communityId);

			assertThat(existingLike.isLiked()).isFalse();
			assertThat(community.getLikeCount()).isZero();
		}
	}

	@Nested
	@DisplayName("getLikeCount")
	class GetLikeCount {

		@Test
		@DisplayName("리포지토리에서 조회한 좋아요 수 반환")
		void returnsCountFromRepository() {
			when(likeRepository.countByCommunityIdAndIsLikedTrue(10L)).thenReturn(42L);

			long result = sut.getLikeCount(10L);

			assertThat(result).isEqualTo(42L);
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
}
