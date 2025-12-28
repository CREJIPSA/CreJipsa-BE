package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatMessageJpaEntity;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
	List<ChatMessageJpaEntity> findByChatRoomId(Long chatRoomId);
}
