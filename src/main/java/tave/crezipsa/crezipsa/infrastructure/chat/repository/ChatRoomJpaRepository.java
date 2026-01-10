package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatRoomJpaEntity;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomJpaEntity, Long> {

	List<ChatRoomJpaEntity> findByUserId(Long userId);
	Optional<ChatRoomJpaEntity> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
	List<ChatRoomJpaEntity> findByUserIdAndChatTitleContaining(Long userId, String keyword);
}
