package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatRoomJpaEntity;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomJpaEntity, Long> {

	List<ChatRoomJpaEntity> findByUserId(Long userId);
	Optional<ChatRoomJpaEntity> findByIdAndUserId(Long id, Long userId);
}
