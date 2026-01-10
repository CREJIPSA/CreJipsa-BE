package tave.crezipsa.crezipsa.domain.chat.port;

import java.util.List;
import java.util.Optional;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;

public interface ChatRoomRepositoryPort {

	ChatRoom save(ChatRoom chatRoom);
	Optional<ChatRoom> findById(Long chatRoomId);
	List<ChatRoom> findByUserId(Long userId);
	List<ChatRoom> searchByTitle(Long userId, String keyword);
	Optional<ChatRoom> findByIdAndUserId(Long chatRoomId, Long userId);
	void changeTitle(Long chatRoomId, Long userId, String title);
}


