package tave.crezipsa.crezipsa.domain.chat.port;

import java.util.Optional;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;

public interface ChatRoomRepositoryPort {

	ChatRoom save(ChatRoom chatRoom);
	Optional<ChatRoom> findById(Long chatRoomId);

}
