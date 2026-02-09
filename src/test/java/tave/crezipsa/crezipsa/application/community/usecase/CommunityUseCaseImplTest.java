package tave.crezipsa.crezipsa.application.community.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
import tave.crezipsa.crezipsa.domain.user.enums.Platform;
import tave.crezipsa.crezipsa.domain.user.repository.UserRepository;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@ExtendWith(MockitoExtension.class)
class CommunityUseCaseImplTest {

	@Mock
	private CommunityRepository communityRepository;
	@Mock
	private LikeRepository likeRepository;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private CommentUsecase commentUsecase;

	@InjectMocks
	private CommunityUseCaseImpl sut;

	@Nested
	@DisplayName("updateCommunity")
	class UpdateCommunity {

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMUNITY 예외")
		void nonWriter_throwsUnauthorized() {
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			assertThatThrownBy(() -> sut.updateCommunity(1L, 999L, new CommunityUpdateRequest()))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void notFound_throwsCommunityNotFound() {
			when(communityRepository.findById(1L)).thenReturn(Optional.empty());

			assertThatThrownBy(() -> sut.updateCommunity(1L, 1L, new CommunityUpdateRequest()))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}

		@Test
		@DisplayName("작성자가 맞으면 정상 업데이트")
		void writer_updatesSuccessfully() {
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			CommunityUpdateRequest request = new CommunityUpdateRequest();
			ReflectionTestUtils.setField(request, "title", "updated title");

			var result = sut.updateCommunity(1L, 100L, request);

			assertThat(result.title()).isEqualTo("updated title");
			assertThat(result.content()).isEqualTo("테스트 내용입니다. 충분히 긴 내용으로 preview 테스트도 가능합니다.");
		}
	}

	@Nested
	@DisplayName("deleteCommunity")
	class DeleteCommunity {

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMUNITY 예외")
		void nonWriter_throwsUnauthorized() {
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			assertThatThrownBy(() -> sut.deleteCommunity(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}

		@Test
		@DisplayName("작성자가 맞으면 정상 삭제")
		void writer_deletesSuccessfully() {
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			sut.deleteCommunity(100L, 1L);

			verify(communityRepository).delete(community);
		}
	}

	@Nested
	@DisplayName("getCommunity")
	class GetCommunity {

		@Test
		@DisplayName("상세 정보를 올바르게 조합 - 다른 사용자가 좋아요한 게시글")
		void assembliesDetailCorrectly() {
			Long communityId = 1L;
			Long writerId = 10L;
			Long viewerId = 20L;

			Community community = createCommunity(communityId, writerId);
			User writer = createUser(writerId);
			Like like = Like.of(viewerId, communityId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(userRepository.findById(writerId)).thenReturn(Optional.of(writer));
			when(commentRepository.countByCommunityId(communityId)).thenReturn(5L);
			when(commentUsecase.getComments(communityId, viewerId)).thenReturn(List.of());
			when(likeRepository.findById(new LikeId(viewerId, communityId))).thenReturn(Optional.of(like));

			CommunityDetailResponse result = sut.getCommunity(communityId, viewerId);

			assertThat(result.communityId()).isEqualTo(communityId);
			assertThat(result.isWriter()).isFalse();
			assertThat(result.isLiked()).isTrue();
			assertThat(result.commentCount()).isEqualTo(5L);
			assertThat(result.writer().userId()).isEqualTo(writerId);
		}

		@Test
		@DisplayName("좋아요 기록 없으면 isLiked=false, 본인 게시글이면 isWriter=true")
		void noLike_isLikedFalse_ownPost_isWriterTrue() {
			Long communityId = 1L;
			Long writerId = 10L;

			Community community = createCommunity(communityId, writerId);
			User writer = createUser(writerId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(userRepository.findById(writerId)).thenReturn(Optional.of(writer));
			when(commentRepository.countByCommunityId(communityId)).thenReturn(0L);
			when(commentUsecase.getComments(communityId, writerId)).thenReturn(List.of());
			when(likeRepository.findById(any(LikeId.class))).thenReturn(Optional.empty());

			CommunityDetailResponse result = sut.getCommunity(communityId, writerId);

			assertThat(result.isLiked()).isFalse();
			assertThat(result.isWriter()).isTrue();
		}
	}

	@Nested
	@DisplayName("searchCommunities")
	class SearchCommunities {

		@Test
		@DisplayName("null 키워드면 SEARCH_KEYWORD_REQUIRED 예외")
		void nullKeyword_throws() {
			assertThatThrownBy(() -> sut.searchCommunities(null, null, "latest", 0, 10))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		@Test
		@DisplayName("빈 키워드면 SEARCH_KEYWORD_REQUIRED 예외")
		void blankKeyword_throws() {
			assertThatThrownBy(() -> sut.searchCommunities("   ", null, "latest", 0, 10))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		@Test
		@DisplayName("30자 초과 키워드면 SEARCH_KEYWORD_TOO_LONG 예외")
		void tooLongKeyword_throws() {
			String longKeyword = "a".repeat(31);

			assertThatThrownBy(() -> sut.searchCommunities(longKeyword, null, "latest", 0, 10))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.SEARCH_KEYWORD_TOO_LONG);
		}
	}

	@Nested
	@DisplayName("getAllCommunities")
	class GetAllCommunities {

		@Test
		@DisplayName("댓글 수를 배치로 로드하여 N+1 방지")
		void batchLoadsCommentCounts() {
			Community c1 = createCommunity(1L, 10L);
			Community c2 = createCommunity(2L, 10L);

			when(communityRepository.findAll()).thenReturn(List.of(c1, c2));
			when(commentRepository.countByCommunityIds(List.of(1L, 2L)))
				.thenReturn(Map.of(1L, 3L, 2L, 7L));

			List<CommunitySummaryResponse> result = sut.getAllCommunities();

			assertThat(result).hasSize(2);
			assertThat(result.get(0).commentCount()).isEqualTo(3L);
			assertThat(result.get(1).commentCount()).isEqualTo(7L);
			verify(commentRepository, times(1)).countByCommunityIds(anyList());
		}

		@Test
		@DisplayName("빈 목록이면 배치 호출하지 않음")
		void emptyList_noBatchCall() {
			when(communityRepository.findAll()).thenReturn(List.of());

			List<CommunitySummaryResponse> result = sut.getAllCommunities();

			assertThat(result).isEmpty();
			verify(commentRepository, never()).countByCommunityIds(anyList());
		}
	}

	// ── 헬퍼 메서드 ──

	private Community createCommunity(Long id, Long writerId) {
		Community community = Community.builder()
			.communityId(id)
			.title("테스트 제목")
			.content("테스트 내용입니다. 충분히 긴 내용으로 preview 테스트도 가능합니다.")
			.field(CommunityField.RECOMMEND)
			.imageUrls(List.of("img1.jpg"))
			.writerId(writerId)
			.likeCount(0L)
			.build();
		ReflectionTestUtils.setField(community, "createdAt", LocalDateTime.now());
		return community;
	}

	private User createUser(Long userId) {
		return User.builder()
			.userId(userId)
			.nickName("testUser")
			.email("test@test.com")
			.profileImageUrl("profile.jpg")
			.mainPlatform(Platform.YOUTUBE)
			.activeYoutube("youtube_channel")
			.build();
	}
}
