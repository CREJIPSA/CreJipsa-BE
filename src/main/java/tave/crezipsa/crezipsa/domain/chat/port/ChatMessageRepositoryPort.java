package tave.crezipsa.crezipsa.domain.chat.port;

import java.util.List;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;

public interface ChatMessageRepositoryPort {

	ChatMessage save(ChatMessage message);
	List<ChatMessage> findByChatRoomId(Long chatRoomId);

}
