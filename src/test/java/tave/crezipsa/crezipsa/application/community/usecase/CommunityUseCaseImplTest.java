package tave.crezipsa.crezipsa.application.community.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.CommunityFixture.*;
import static tave.crezipsa.crezipsa.fixture.UserFixture.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import tave.crezipsa.crezipsa.application.community.dto.request.CommunityCreateRequest;
import tave.crezipsa.crezipsa.application.community.dto.request.CommunityUpdateRequest;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityDetailResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunityResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.CommunitySummaryResponse;
import tave.crezipsa.crezipsa.application.community.dto.response.MyCommunityResponse;
import tave.crezipsa.crezipsa.domain.community.domain.Community;
import tave.crezipsa.crezipsa.domain.community.domain.CommunityField;
import tave.crezipsa.crezipsa.domain.community.domain.Like;
import tave.crezipsa.crezipsa.domain.community.domain.LikeId;
import tave.crezipsa.crezipsa.domain.community.repository.CommentRepository;
import tave.crezipsa.crezipsa.domain.community.repository.CommunityRepository;
import tave.crezipsa.crezipsa.domain.community.repository.LikeRepository;
import tave.crezipsa.crezipsa.domain.user.entity.User;
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
	@DisplayName("createCommunity")
	class CreateCommunity {

		@Test
		@DisplayName("정상 요청이면 커뮤니티 생성 후 응답 반환")
		void success() {
			// given
			Long userId = 1L;
			CommunityCreateRequest request = new CommunityCreateRequest();
			ReflectionTestUtils.setField(request, "title", "제목");
			ReflectionTestUtils.setField(request, "content", "내용");
			ReflectionTestUtils.setField(request, "field", CommunityField.RECOMMEND);

			when(communityRepository.save(any(Community.class))).thenAnswer(invocation -> {
				Community c = invocation.getArgument(0);
				ReflectionTestUtils.setField(c, "communityId", 100L);
				return c;
			});

			// when
			CommunityResponse result = sut.createCommunity(userId, request);

			// then
			assertThat(result.communityId()).isEqualTo(100L);
			assertThat(result.title()).isEqualTo("제목");
			assertThat(result.field()).isEqualTo(CommunityField.RECOMMEND);
			verify(communityRepository).save(any(Community.class));
		}
	}

	@Nested
	@DisplayName("updateCommunity")
	class UpdateCommunity {

		@Test
		@DisplayName("작성자가 아니면 UNAUTHORIZED_COMMUNITY 예외")
		void nonWriter_throwsUnauthorized() {
			// given
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			// when & then
			assertThatThrownBy(() -> sut.updateCommunity(1L, 999L, new CommunityUpdateRequest()))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void notFound_throwsCommunityNotFound() {
			// given
			when(communityRepository.findById(1L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.updateCommunity(1L, 1L, new CommunityUpdateRequest()))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}

		@Test
		@DisplayName("작성자가 맞으면 정상 업데이트")
		void writer_updatesSuccessfully() {
			// given
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			CommunityUpdateRequest request = new CommunityUpdateRequest();
			ReflectionTestUtils.setField(request, "title", "updated title");

			// when
			var result = sut.updateCommunity(1L, 100L, request);

			// then
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
			// given
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			// when & then
			assertThatThrownBy(() -> sut.deleteCommunity(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.UNAUTHORIZED_COMMUNITY);
		}

		@Test
		@DisplayName("작성자가 맞으면 정상 삭제")
		void writer_deletesSuccessfully() {
			// given
			Community community = createCommunity(1L, 100L);
			when(communityRepository.findById(1L)).thenReturn(Optional.of(community));

			// when
			sut.deleteCommunity(100L, 1L);

			// then
			verify(communityRepository).delete(community);
		}
	}

	@Nested
	@DisplayName("getCommunity")
	class GetCommunity {

		@Test
		@DisplayName("상세 정보를 올바르게 조합 - 다른 사용자가 좋아요한 게시글")
		void assembliesDetailCorrectly() {
			// given
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

			// when
			CommunityDetailResponse result = sut.getCommunity(communityId, viewerId);

			// then
			assertThat(result.communityId()).isEqualTo(communityId);
			assertThat(result.isWriter()).isFalse();
			assertThat(result.isLiked()).isTrue();
			assertThat(result.commentCount()).isEqualTo(5L);
			assertThat(result.writer().userId()).isEqualTo(writerId);
		}

		@Test
		@DisplayName("존재하지 않는 게시글이면 COMMUNITY_NOT_FOUND 예외")
		void communityNotFound_throws() {
			// given
			when(communityRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.getCommunity(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.COMMUNITY_NOT_FOUND);
		}

		@Test
		@DisplayName("작성자가 존재하지 않으면 USER_NOT_FOUND 예외")
		void writerNotFound_throws() {
			// given
			Long communityId = 1L;
			Community community = createCommunity(communityId, 999L);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(userRepository.findById(999L)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> sut.getCommunity(communityId, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.USER_NOT_FOUND);
		}

		@Test
		@DisplayName("좋아요 기록 없으면 isLiked=false, 본인 게시글이면 isWriter=true")
		void noLike_isLikedFalse_ownPost_isWriterTrue() {
			// given
			Long communityId = 1L;
			Long writerId = 10L;

			Community community = createCommunity(communityId, writerId);
			User writer = createUser(writerId);

			when(communityRepository.findById(communityId)).thenReturn(Optional.of(community));
			when(userRepository.findById(writerId)).thenReturn(Optional.of(writer));
			when(commentRepository.countByCommunityId(communityId)).thenReturn(0L);
			when(commentUsecase.getComments(communityId, writerId)).thenReturn(List.of());
			when(likeRepository.findById(any(LikeId.class))).thenReturn(Optional.empty());

			// when
			CommunityDetailResponse result = sut.getCommunity(communityId, writerId);

			// then
			assertThat(result.isLiked()).isFalse();
			assertThat(result.isWriter()).isTrue();
		}
	}

	@Nested
	@DisplayName("getMyCommunities")
	class GetMyCommunities {

		@Test
		@DisplayName("latest 정렬 시 findMyCommunitiesLatest 호출")
		void latestSort_callsLatestRepo() {
			// given
			Community c1 = createCommunity(1L, 10L);
			Page<Community> page = new PageImpl<>(List.of(c1), PageRequest.of(0, 10), 1);

			when(communityRepository.findMyCommunitiesLatest(eq(10L), eq(CommunityField.RECOMMEND), any()))
				.thenReturn(page);
			when(commentRepository.countByCommunityIds(List.of(1L))).thenReturn(Map.of(1L, 3L));

			// when
			List<MyCommunityResponse> result = sut.getMyCommunities(10L, CommunityField.RECOMMEND, "latest", 0, 10);

			// then
			assertThat(result).hasSize(1);
			verify(communityRepository).findMyCommunitiesLatest(eq(10L), eq(CommunityField.RECOMMEND), any());
			verify(communityRepository, never()).findMyCommunitiesPopular(any(), any(), any());
		}

		@Test
		@DisplayName("popular 정렬 시 findMyCommunitiesPopular 호출")
		void popularSort_callsPopularRepo() {
			// given
			Community c1 = createCommunity(1L, 10L);
			Page<Community> page = new PageImpl<>(List.of(c1), PageRequest.of(0, 10), 1);

			when(communityRepository.findMyCommunitiesPopular(eq(10L), eq(CommunityField.TIP), any()))
				.thenReturn(page);
			when(commentRepository.countByCommunityIds(List.of(1L))).thenReturn(Map.of(1L, 0L));

			// when
			List<MyCommunityResponse> result = sut.getMyCommunities(10L, CommunityField.TIP, "popular", 0, 10);

			// then
			assertThat(result).hasSize(1);
			verify(communityRepository).findMyCommunitiesPopular(eq(10L), eq(CommunityField.TIP), any());
			verify(communityRepository, never()).findMyCommunitiesLatest(any(), any(), any());
		}
	}

	@Nested
	@DisplayName("getCommunitiesByField")
	class GetCommunitiesByField {

		@Test
		@DisplayName("필드별 커뮤니티 조회 시 댓글 수 포함하여 반환")
		void returnsByFieldWithCommentCounts() {
			// given
			Community c1 = createCommunity(1L, 10L);
			when(communityRepository.findByField(CommunityField.RECOMMEND)).thenReturn(List.of(c1));
			when(commentRepository.countByCommunityIds(List.of(1L))).thenReturn(Map.of(1L, 2L));

			// when
			List<CommunitySummaryResponse> result = sut.getCommunitiesByField(CommunityField.RECOMMEND);

			// then
			assertThat(result).hasSize(1);
			assertThat(result.get(0).commentCount()).isEqualTo(2L);
		}
	}

	@Nested
	@DisplayName("searchCommunities")
	class SearchCommunities {

		@Test
		@DisplayName("null 키워드면 SEARCH_KEYWORD_REQUIRED 예외")
		void nullKeyword_throws() {
			// when & then
			assertThatThrownBy(() -> sut.searchCommunities(null, null, "latest", 0, 10))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		@Test
		@DisplayName("빈 키워드면 SEARCH_KEYWORD_REQUIRED 예외")
		void blankKeyword_throws() {
			// when & then
			assertThatThrownBy(() -> sut.searchCommunities("   ", null, "latest", 0, 10))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.SEARCH_KEYWORD_REQUIRED);
		}

		@Test
		@DisplayName("30자 초과 키워드면 SEARCH_KEYWORD_TOO_LONG 예외")
		void tooLongKeyword_throws() {
			// given
			String longKeyword = "a".repeat(31);

			// when & then
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
			// given
			Community c1 = createCommunity(1L, 10L);
			Community c2 = createCommunity(2L, 10L);

			when(communityRepository.findAll()).thenReturn(List.of(c1, c2));
			when(commentRepository.countByCommunityIds(List.of(1L, 2L)))
				.thenReturn(Map.of(1L, 3L, 2L, 7L));

			// when
			List<CommunitySummaryResponse> result = sut.getAllCommunities();

			// then
			assertThat(result).hasSize(2);
			assertThat(result.get(0).commentCount()).isEqualTo(3L);
			assertThat(result.get(1).commentCount()).isEqualTo(7L);
			verify(commentRepository, times(1)).countByCommunityIds(anyList());
		}

		@Test
		@DisplayName("빈 목록이면 배치 호출하지 않음")
		void emptyList_noBatchCall() {
			// given
			when(communityRepository.findAll()).thenReturn(List.of());

			// when
			List<CommunitySummaryResponse> result = sut.getAllCommunities();

			// then
			assertThat(result).isEmpty();
			verify(commentRepository, never()).countByCommunityIds(anyList());
		}
	}
}
