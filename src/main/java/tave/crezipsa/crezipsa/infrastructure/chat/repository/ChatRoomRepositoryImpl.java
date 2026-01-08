package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.infrastructure.chat.mapper.ChatRoomMapper;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryPort {

	private final ChatRoomJpaRepository chatRoomJpaRepository;

	@Override
	public ChatRoom save(ChatRoom chatRoom) {
		return ChatRoomMapper.toDomain(
			chatRoomJpaRepository.save(ChatRoomMapper.toJpa(chatRoom))
		);
	}

	@Override
	public Optional<ChatRoom> findById(Long chatRoomId) {
		return chatRoomJpaRepository.findById(chatRoomId)
			.map(ChatRoomMapper::toDomain);
	}

	@Override
	public List<ChatRoom> findByUserId(Long userId) {
		return chatRoomJpaRepository.findByUserId(userId).stream()
			.map(ChatRoomMapper::toDomain)
			.toList();
	}

	@Override
	public Optional<ChatRoom> findByIdAndUserId(Long chatRoomId, Long userId) {
		return chatRoomJpaRepository
			.findByIdAndUserId(chatRoomId, userId)
			.map(ChatRoomMapper::toDomain);
	}
}
