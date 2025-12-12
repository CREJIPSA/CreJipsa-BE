package tave.crezipsa.crezipsa.infrastructure.chat.mapper;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatMessageJpaEntity;

public class ChatMessageMapper {

	public static ChatMessage toDomain(ChatMessageJpaEntity e) {
		return ChatMessage.builder()
			.id(e.getMessageId())
			.chatRoomId(e.getChatRoomId())
			.senderType(ChatMessage.SenderType.valueOf(e.getSenderType().name()))
			.content(e.getContent())
			.build();
	}
}
