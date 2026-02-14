package tave.crezipsa.crezipsa.application.chat.usecase;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static tave.crezipsa.crezipsa.fixture.ChatFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
