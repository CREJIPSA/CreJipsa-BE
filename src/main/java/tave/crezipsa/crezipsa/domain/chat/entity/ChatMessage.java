package tave.crezipsa.crezipsa.domain.chat.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChatMessage {

	public enum SenderType {
		USER,
		AI
	}

	private final Long id;
	private final Long chatRoomId;
	private final SenderType senderType;
	private final String content;
	private final LocalDateTime createdAt;

	public static ChatMessage fromUser(Long chatRoomId, String content) {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderType(SenderType.USER)
			.content(content)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ChatMessage fromAI(Long chatRoomId, String content) {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderType(SenderType.AI)
			.content(content)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
