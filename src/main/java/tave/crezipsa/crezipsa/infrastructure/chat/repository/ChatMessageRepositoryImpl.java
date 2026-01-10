package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.infrastructure.chat.mapper.ChatMessageMapper;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryPort {

	private final ChatMessageJpaRepository chatMessageJpaRepository;

	@Override
	public ChatMessage save(ChatMessage message) {
		return ChatMessageMapper.toDomain(
			chatMessageJpaRepository.save(ChatMessageMapper.toJpa(message))
		);
	}

	@Override
	public List<ChatMessage> findByChatRoomId(Long chatRoomId) {
		return chatMessageJpaRepository.findByChatRoomId(chatRoomId).stream()
			.map(ChatMessageMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public ChatMessage findById(Long messageId) {
		return chatMessageJpaRepository.findById(messageId)
			.map(ChatMessageMapper::toDomain)
			.orElse(null);
	}

	@Override
	public List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId) {
		return chatMessageJpaRepository
			.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
			.stream()
			.map(ChatMessageMapper::toDomain)
			.toList();
	}

	@Override
	public Optional<LocalDateTime> findLastMessageAt(Long chatRoomId) {
		return chatMessageJpaRepository.findLastMessageAt(chatRoomId);
	}
}
