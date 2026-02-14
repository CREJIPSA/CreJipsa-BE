package tave.crezipsa.crezipsa.application.storyboard.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.ChatFixture.*;
import static tave.crezipsa.crezipsa.fixture.StoryboardFixture.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tave.crezipsa.crezipsa.application.storyboard.dto.request.CreateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardTitleRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardCutResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardEditorResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardCutRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@ExtendWith(MockitoExtension.class)
class StoryboardUsecaseImplTest {

	@Mock
	private StoryboardRepositoryPort storyboardRepository;
	@Mock
	private StoryboardCutRepositoryPort storyboardCutRepository;
	@Mock
	private ChatMessageRepositoryPort chatMessageRepository;

	@InjectMocks
	private StoryboardUsecaseImpl sut;

	@Nested
	@DisplayName("create")
	class Create {

		@Test
		@DisplayName("chatMessageId가 null이면 검증 없이 스토리보드 생성")
		void noChatMessage_createsSuccessfully() {
			// given
			Long userId = 1L;
			CreateStoryboardRequest request = new CreateStoryboardRequest("제목", null);

			Storyboard saved = createStoryboard(10L, userId);
			StoryboardCut firstCut = createStoryboardCut(100L, 10L, 0);

			when(storyboardRepository.save(any(Storyboard.class))).thenReturn(saved);
			when(storyboardCutRepository.findNextOrder(10L)).thenReturn(0);
			when(storyboardCutRepository.save(any(StoryboardCut.class))).thenReturn(firstCut);

			// when
			StoryboardEditorResponse result = sut.create(userId, request);

			// then
			assertThat(result.storyboardId()).isEqualTo(10L);
			assertThat(result.cuts()).hasSize(1);
			verify(chatMessageRepository, never()).findById(anyLong());
		}

		@Test
		@DisplayName("chatMessageId가 존재하지 않으면 CHAT_NOT_FOUND 예외")
		void chatMessageNotFound_throws() {
			// given
			Long userId = 1L;
			CreateStoryboardRequest request = new CreateStoryboardRequest("제목", 999L);

			when(chatMessageRepository.findById(999L)).thenReturn(null);

			// when & then
			assertThatThrownBy(() -> sut.create(userId, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.CHAT_NOT_FOUND);
		}

		@Test
		@DisplayName("chatMessage의 senderType이 AI가 아니면 INVALID_SENDER_TYPE 예외")
		void userSenderType_throws() {
			// given
			Long userId = 1L;
			CreateStoryboardRequest request = new CreateStoryboardRequest("제목", 50L);

			ChatMessage userMessage = createUserMessage(50L, 1L);
			when(chatMessageRepository.findById(50L)).thenReturn(userMessage);

			// when & then
			assertThatThrownBy(() -> sut.create(userId, request))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_SENDER_TYPE);
		}

		@Test
		@DisplayName("AI 메시지로부터 스토리보드 정상 생성")
		void fromAiMessage_createsSuccessfully() {
			// given
			Long userId = 1L;
			CreateStoryboardRequest request = new CreateStoryboardRequest("제목", 50L);

			ChatMessage aiMessage = createAiMessage(50L, 1L);
			Storyboard saved = createStoryboard(10L, userId, 50L);
			StoryboardCut firstCut = createStoryboardCut(100L, 10L, 0);

			when(chatMessageRepository.findById(50L)).thenReturn(aiMessage);
			when(storyboardRepository.save(any(Storyboard.class))).thenReturn(saved);
			when(storyboardCutRepository.findNextOrder(10L)).thenReturn(0);
			when(storyboardCutRepository.save(any(StoryboardCut.class))).thenReturn(firstCut);

			// when
			StoryboardEditorResponse result = sut.create(userId, request);

			// then
			assertThat(result.storyboardId()).isEqualTo(10L);
			assertThat(result.sourceChatMessageId()).isEqualTo(50L);
			assertThat(result.cuts()).hasSize(1);
		}
	}

	@Nested
	@DisplayName("get")
	class Get {

		@Test
		@DisplayName("스토리보드가 없으면 STORYBOARD_NOT_FOUND 예외")
		void notFound_throws() {
			// given
			when(storyboardRepository.findById(1L)).thenReturn(null);

			// when & then
			assertThatThrownBy(() -> sut.get(1L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("다른 사용자의 스토리보드면 STORYBOARD_NOT_FOUND 예외")
		void otherUser_throws() {
			// given
			Storyboard sb = createStoryboard(1L, 100L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when & then
			assertThatThrownBy(() -> sut.get(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("본인 스토리보드면 컷 목록과 함께 정상 반환")
		void owner_returnsWithCuts() {
			// given
			Long userId = 1L;
			Long storyboardId = 10L;
			Storyboard sb = createStoryboard(storyboardId, userId);
			StoryboardCut cut1 = createStoryboardCut(100L, storyboardId, 0);
			StoryboardCut cut2 = createStoryboardCut(101L, storyboardId, 1);

			when(storyboardRepository.findById(storyboardId)).thenReturn(sb);
			when(storyboardCutRepository.findByStoryboardId(storyboardId)).thenReturn(List.of(cut1, cut2));

			// when
			StoryboardEditorResponse result = sut.get(userId, storyboardId);

			// then
			assertThat(result.storyboardId()).isEqualTo(storyboardId);
			assertThat(result.cuts()).hasSize(2);
			assertThat(result.cuts().get(0).cutId()).isEqualTo(100L);
			assertThat(result.cuts().get(1).cutId()).isEqualTo(101L);
		}
	}

	@Nested
	@DisplayName("getMyList")
	class GetMyList {

		@Test
		@DisplayName("스토리보드가 없으면 빈 목록 반환")
		void noStoryboards_returnsEmpty() {
			// given
			when(storyboardRepository.findByUserId(1L)).thenReturn(List.of());

			// when
			List<StoryboardSummaryResponse> result = sut.getMyList(1L);

			// then
			assertThat(result).isEmpty();
		}

		@Test
		@DisplayName("스토리보드 목록 정상 반환")
		void hasStoryboards_returnsList() {
			// given
			Long userId = 1L;
			Storyboard sb1 = createStoryboard(10L, userId);
			Storyboard sb2 = createStoryboard(11L, userId);

			when(storyboardRepository.findByUserId(userId)).thenReturn(List.of(sb1, sb2));

			// when
			List<StoryboardSummaryResponse> result = sut.getMyList(userId);

			// then
			assertThat(result).hasSize(2);
			assertThat(result.get(0).storyboardId()).isEqualTo(10L);
			assertThat(result.get(1).storyboardId()).isEqualTo(11L);
		}
	}

	@Nested
	@DisplayName("updateTitle")
	class UpdateTitle {

		@Test
		@DisplayName("스토리보드가 없으면 STORYBOARD_NOT_FOUND 예외")
		void notFound_throws() {
			// given
			when(storyboardRepository.findById(1L)).thenReturn(null);

			// when & then
			assertThatThrownBy(() -> sut.updateTitle(1L, 1L, new UpdateStoryboardTitleRequest("새 제목")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("다른 사용자면 STORYBOARD_NOT_FOUND 예외")
		void otherUser_throws() {
			// given
			Storyboard sb = createStoryboard(1L, 100L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when & then
			assertThatThrownBy(() -> sut.updateTitle(999L, 1L, new UpdateStoryboardTitleRequest("새 제목")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("제목이 null이면 INVALID_INPUT_VALUE 예외")
		void nullTitle_throws() {
			// given
			Storyboard sb = createStoryboard(1L, 1L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when & then
			assertThatThrownBy(() -> sut.updateTitle(1L, 1L, new UpdateStoryboardTitleRequest(null)))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_INPUT_VALUE);
		}

		@Test
		@DisplayName("제목이 빈 문자열이면 INVALID_INPUT_VALUE 예외")
		void blankTitle_throws() {
			// given
			Storyboard sb = createStoryboard(1L, 1L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when & then
			assertThatThrownBy(() -> sut.updateTitle(1L, 1L, new UpdateStoryboardTitleRequest("   ")))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.INVALID_INPUT_VALUE);
		}

		@Test
		@DisplayName("정상 제목이면 업데이트 성공")
		void validTitle_updates() {
			// given
			Storyboard sb = createStoryboard(1L, 1L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when
			sut.updateTitle(1L, 1L, new UpdateStoryboardTitleRequest("새 제목"));

			// then
			verify(storyboardRepository).save(any(Storyboard.class));
		}
	}

	@Nested
	@DisplayName("addCut")
	class AddCut {

		@Test
		@DisplayName("스토리보드가 없으면 STORYBOARD_NOT_FOUND 예외")
		void notFound_throws() {
			// given
			when(storyboardRepository.findById(1L)).thenReturn(null);

			// when & then
			assertThatThrownBy(() -> sut.addCut(1L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("다른 사용자면 STORYBOARD_NOT_FOUND 예외")
		void otherUser_throws() {
			// given
			Storyboard sb = createStoryboard(1L, 100L);
			when(storyboardRepository.findById(1L)).thenReturn(sb);

			// when & then
			assertThatThrownBy(() -> sut.addCut(999L, 1L))
				.isInstanceOf(CommonException.class)
				.extracting("errorCode")
				.isEqualTo(ErrorCode.STORYBOARD_NOT_FOUND);
		}

		@Test
		@DisplayName("본인 스토리보드면 컷 추가 성공")
		void owner_addsCut() {
			// given
			Long userId = 1L;
			Long storyboardId = 10L;
			Storyboard sb = createStoryboard(storyboardId, userId);
			StoryboardCut newCut = createStoryboardCut(200L, storyboardId, 2);

			when(storyboardRepository.findById(storyboardId)).thenReturn(sb);
			when(storyboardCutRepository.findNextOrder(storyboardId)).thenReturn(2);
			when(storyboardCutRepository.save(any(StoryboardCut.class))).thenReturn(newCut);

			// when
			StoryboardCutResponse result = sut.addCut(userId, storyboardId);

			// then
			assertThat(result.cutId()).isEqualTo(200L);
			assertThat(result.order()).isEqualTo(2);
		}
	}
}
