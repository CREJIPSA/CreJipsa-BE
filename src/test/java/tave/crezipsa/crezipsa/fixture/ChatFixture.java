package tave.crezipsa.crezipsa.fixture;

import java.time.LocalDateTime;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;

public final class ChatFixture {

	private ChatFixture() {
	}

	public static ChatRoom createChatRoom(Long id, Long userId) {
		return ChatRoom.builder()
			.id(id)
			.userId(userId)
			.title("테스트 채팅방")
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ChatMessage createUserMessage(Long id, Long chatRoomId) {
		return ChatMessage.builder()
			.id(id)
			.chatRoomId(chatRoomId)
			.senderType(ChatMessage.SenderType.USER)
			.content("사용자 메시지")
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ChatMessage createAiMessage(Long id, Long chatRoomId) {
		return ChatMessage.builder()
			.id(id)
			.chatRoomId(chatRoomId)
			.senderType(ChatMessage.SenderType.AI)
			.content("AI 응답 메시지")
			.createdAt(LocalDateTime.now())
			.build();
	}
}
