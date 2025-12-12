package tave.crezipsa.crezipsa.infrastructure.chat.mapper;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatRoomJpaEntity;

public class ChatRoomMapper {

	public static ChatRoom toDomain(ChatRoomJpaEntity e) {
		return ChatRoom.builder()
			.id(e.getChatRoomId())
			.userId(e.getUserId())
			.title(e.getChatTitle())
			.build();
	}

}
