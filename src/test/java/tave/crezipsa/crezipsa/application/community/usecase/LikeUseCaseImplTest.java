package tave.crezipsa.crezipsa.application.community.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.CommunityFixture.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import tave.crezipsa.crezipsa.application.community.cache.CommunityCacheService;
import tave.crezipsa.crezipsa.application.community.dto.response.MyLikedCommunityResponse;
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
	@Mock
	private CommunityCacheService communityCacheService;

	@InjectMocks
	private LikeUseCaseImpl sut;

	@Nested
	@DisplayName("like")
	class LikeAction {

		@Test
		@DisplayName("새로운 좋아요 - Like 생성 후 커뮤니티 likeCount 증가")
		void newLike_createsAndIncrements() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.empty());

			// when
			sut.like(userId, communityId);

			// then
			verify(likeRepository).save(any(Like.class));
			assertThat(community.getLikeCount()).isEqualTo(1L);
		}

		@Test
		@DisplayName("이미 좋아요 상태 - unlike으로 토글, likeCount 감소")
		void existingLiked_togglesOff() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);
			community.increaseLikeCount();

			Like existingLike = Like.of(userId, communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			// when
			sut.like(userId, communityId);

			// then
			assertThat(existingLike.isLiked()).isFalse();
			assertThat(community.getLikeCount()).isZero();
			verify(likeRepository, never()).save(any());
			verify(communityCacheService).evictCommunityAll(communityId);
		}

		@Test
		@DisplayName("좋아요 취소 상태에서 다시 좋아요 - like으로 토글, likeCount 증가")
		void existingUnliked_togglesOn() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			Like existingLike = Like.of(userId, communityId);
			existingLike.unlike();

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			// when
			sut.like(userId, communityId);

			// then
			assertThat(existingLike.isLiked()).isTrue();
			assertThat(community.getLikeCount()).isEqualTo(1L);
			verify(communityCacheService).evictCommunityAll(communityId);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			// given
			when(communityRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
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
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			// given
			when(communityRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.unlike(1L, 999L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}

		@Test
		@DisplayName("좋아요 기록이 없으면 NOT_LIKED 예외")
		void noLikeRecord_throwsNotLiked() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.unlike(userId, communityId))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.NOT_LIKED);
		}

		@Test
		@DisplayName("이미 unlike 상태면 NOT_LIKED 예외")
		void alreadyUnliked_throwsNotLiked() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);

			Like existingLike = Like.of(userId, communityId);
			existingLike.unlike();

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			// when & then
			assertThatThrownBy(() -> sut.unlike(userId, communityId))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.NOT_LIKED);
		}

		@Test
		@DisplayName("정상 unlike - isLiked=false로 변경, likeCount 감소")
		void liked_decrementsCount() {
			// given
			Long userId = 1L;
			Long communityId = 10L;
			Community community = createCommunity(communityId);
			community.increaseLikeCount();

			Like existingLike = Like.of(userId, communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(likeRepository.findById(new LikeId(userId, communityId))).thenReturn(Optional.of(existingLike));

			// when
			sut.unlike(userId, communityId);

			// then
			assertThat(existingLike.isLiked()).isFalse();
			assertThat(community.getLikeCount()).isZero();
			verify(communityCacheService).evictCommunityAll(communityId);
		}
	}

	@Nested
	@DisplayName("getLikeCount")
	class GetLikeCount {

		@Test
		@DisplayName("리포지토리에서 조회한 좋아요 수 반환")
		void returnsCountFromRepository() {
			// given
			when(likeRepository.countByCommunityIdAndIsLikedTrue(10L)).thenReturn(42L);

			// when
			long result = sut.getLikeCount(10L);

			// then
			assertThat(result).isEqualTo(42L);
		}
	}

	@Nested
	@DisplayName("getMyLikedCommunities")
	class GetMyLikedCommunities {

		@Test
		@DisplayName("latest 정렬 시 findMyLikedCommunitiesLatest 호출")
		void latestSort() {
			// given
			Community c1 = createCommunity(1L, 5L);
			Page<Community> page = new PageImpl<>(List.of(c1), PageRequest.of(0, 10), 1);

			when(likeRepository.findMyLikedCommunitiesLatest(eq(10L), isNull(), any()))
				.thenReturn(page);
			when(commentRepository.countByCommunityId(1L)).thenReturn(3L);

			// when
			Slice<MyLikedCommunityResponse> result = sut.getMyLikedCommunities(10L, null, "latest", PageRequest.of(0, 10));

			// then
			assertThat(result.getContent()).hasSize(1);
			assertThat(result.getContent().get(0).commentCount()).isEqualTo(3L);
			verify(likeRepository).findMyLikedCommunitiesLatest(eq(10L), isNull(), any());
		}

		@Test
		@DisplayName("popular 정렬 시 findMyLikedCommunitiesPopular 호출")
		void popularSort() {
			// given
			Community c1 = createCommunity(1L, 5L);
			Page<Community> page = new PageImpl<>(List.of(c1), PageRequest.of(0, 10), 1);

			when(likeRepository.findMyLikedCommunitiesPopular(eq(10L), eq(CommunityField.TIP), any()))
				.thenReturn(page);
			when(commentRepository.countByCommunityId(1L)).thenReturn(0L);

			// when
			Slice<MyLikedCommunityResponse> result = sut.getMyLikedCommunities(10L, CommunityField.TIP, "popular", PageRequest.of(0, 10));

			// then
			assertThat(result.getContent()).hasSize(1);
			verify(likeRepository).findMyLikedCommunitiesPopular(eq(10L), eq(CommunityField.TIP), any());
		}
	}
}
