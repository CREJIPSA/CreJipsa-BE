package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.infrastructure.chat.entity.ChatRoomJpaEntity;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomJpaEntity, Long> {

}
