package tave.crezipsa.crezipsa.infrastructure.chat.repository;

import java.util.List;
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
}
