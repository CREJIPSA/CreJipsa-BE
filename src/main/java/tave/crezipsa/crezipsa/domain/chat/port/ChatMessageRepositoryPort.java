package tave.crezipsa.crezipsa.domain.chat.port;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;

public interface ChatMessageRepositoryPort {

	ChatMessage save(ChatMessage message);
	List<ChatMessage> findByChatRoomId(Long chatRoomId);
	ChatMessage findById(Long id);
	List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);
	Optional<LocalDateTime> findLastMessageAt(Long chatRoomId);

}
