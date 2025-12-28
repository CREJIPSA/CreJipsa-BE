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

	public static ChatMessageJpaEntity toJpa(ChatMessage d) {
		return ChatMessageJpaEntity.builder()
			.messageId(d.getId())
			.chatRoomId(d.getChatRoomId())
			.senderType(ChatMessageJpaEntity.SenderType.valueOf(d.getSenderType().name()))
			.content(d.getContent())
			.build();
	}

}
