package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatMessageJpaEntity;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
	interface ChatRoomLastMessageAtProjection {
		Long getChatRoomId();
		LocalDateTime getLastMessageAt();
	}

	List<ChatMessageJpaEntity> findByChatRoomId(Long chatRoomId);

	List<ChatMessageJpaEntity>
	findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

	@Query("""
		select max(m.createdAt)
		from ChatMessageJpaEntity m
		where m.chatRoomId = :chatRoomId
	""")
	Optional<LocalDateTime> findLastMessageAt(Long chatRoomId);

	@Query("""
		select m.chatRoomId as chatRoomId, max(m.createdAt) as lastMessageAt
		from ChatMessageJpaEntity m
		where m.chatRoomId in :chatRoomIds
		group by m.chatRoomId
	""")
	List<ChatRoomLastMessageAtProjection> findLastMessageAtByChatRoomIds(
		@Param("chatRoomIds") List<Long> chatRoomIds
	);
}
