package tave.crezipsa.crezipsa.application.chat.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.ChatFixture.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tave.crezipsa.crezipsa.application.chat.dto.response.ChatRoomListResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;

@ExtendWith(MockitoExtension.class)
class ChatUseCaseImplTest {

	@Mock
	private ChatRoomRepositoryPort chatRoomRepository;
	@Mock
	private ChatMessageRepositoryPort chatMessageRepository;
	@Mock
	private StoryboardGeneratorPort storyboardGeneratorPort;

	@InjectMocks
	private ChatUseCaseImpl sut;

	@Nested
	@DisplayName("createChatRoom")
	class CreateChatRoom {

		@Test
		@DisplayName("제목이 null이면 기본 제목으로 생성")
		void nullTitle_usesDefault() {
			// given
			ChatRoom saved = createChatRoom(1L, 1L);
			when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(saved);

			// when
			Long result = sut.createChatRoom(1L, null);

			// then
			assertThat(result).isEqualTo(1L);
		}

		@Test
		@DisplayName("제목이 빈 문자열이면 기본 제목으로 생성")
		void blankTitle_usesDefault() {
			// given
			ChatRoom saved = createChatRoom(1L, 1L);
			when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(saved);

			// when
			Long result = sut.createChatRoom(1L, "   ");

			// then
			assertThat(result).isEqualTo(1L);
		}

		@Test
		@DisplayName("제목이 있으면 해당 제목으로 생성")
		void validTitle_createsWithTitle() {
			// given
			ChatRoom saved = createChatRoom(1L, 1L);
			when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(saved);

			// when
			Long result = sut.createChatRoom(1L, "내 채팅방");

			// then
			assertThat(result).isEqualTo(1L);
		}
	}

	@Nested
	@DisplayName("sendUserMessage")
	class SendUserMessage {

		@Test
		@DisplayName("사용자 메시지 저장 후 AI 응답 저장 및 반환")
		void savesUserAndAiMessages() {
			// given
			Long chatRoomId = 1L;
			Long userId = 1L;
			String userMsg = "스토리보드 만들어줘";
			String aiReply = "AI 응답입니다";

			when(chatMessageRepository.save(any(ChatMessage.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
			when(storyboardGeneratorPort.generate(userMsg)).thenReturn(aiReply);

			// when
			GeminiChatResponse result = sut.sendUserMessage(chatRoomId, userId, userMsg);

			// then
			assertThat(result.content()).isEqualTo(aiReply);
			verify(chatMessageRepository, times(2)).save(any(ChatMessage.class));
			verify(storyboardGeneratorPort).generate(userMsg);
		}
	}

	@Nested
	@DisplayName("changeChatRoomTitle")
	class ChangeChatRoomTitle {

		@Test
		@DisplayName("채팅방 제목 변경 위임")
		void delegatesToRepository() {
			// when
			sut.changeChatRoomTitle(1L, 10L, "새 제목");

			// then
			verify(chatRoomRepository).changeTitle(10L, 1L, "새 제목");
		}
	}

	@Nested
	@DisplayName("getMyChatRooms")
	class GetMyChatRooms {

		@Test
		@DisplayName("채팅방이 없으면 빈 목록 반환")
		void noRooms_returnsEmpty() {
			// given
			when(chatRoomRepository.findByUserId(1L)).thenReturn(List.of());

			// when
			List<ChatRoomListResponse> result = sut.getMyChatRooms(1L);

			// then
			assertThat(result).isEmpty();
			verify(chatMessageRepository, never()).findLastMessageAtByChatRoomIds(anyList());
		}

		@Test
		@DisplayName("채팅방 목록과 마지막 메시지 시간 조합하여 반환")
		void hasRooms_returnsWithLastMessageAt() {
			// given
			Long userId = 1L;
			ChatRoom room1 = createChatRoom(10L, userId);
			ChatRoom room2 = createChatRoom(11L, userId);
			LocalDateTime now = LocalDateTime.now();

			when(chatRoomRepository.findByUserId(userId)).thenReturn(List.of(room1, room2));
			when(chatMessageRepository.findLastMessageAtByChatRoomIds(List.of(10L, 11L)))
				.thenReturn(Map.of(10L, now, 11L, now.minusHours(1)));

			// when
			List<ChatRoomListResponse> result = sut.getMyChatRooms(userId);

			// then
			assertThat(result).hasSize(2);
			assertThat(result.get(0).chatRoomId()).isEqualTo(10L);
			assertThat(result.get(0).lastMessageAt()).isEqualTo(now);
			assertThat(result.get(1).chatRoomId()).isEqualTo(11L);
		}
	}
}
