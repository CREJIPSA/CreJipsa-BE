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
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardEditorResponse;
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
}
