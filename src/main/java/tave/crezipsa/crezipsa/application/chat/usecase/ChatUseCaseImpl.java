package tave.crezipsa.crezipsa.application.chat.usecase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.StoryboardGeneratorPort;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatUseCaseImpl implements ChatUseCase {

	private final ChatRoomRepositoryPort chatRoomRepository;
	private final ChatMessageRepositoryPort chatMessageRepository;
	private final StoryboardRepositoryPort storyboardRepository;


	@Override
	public Long createChatRoom(Long userId, String title) {
		ChatRoom room = ChatRoom.create(userId, title);
		return chatRoomRepository.save(room).getId();
	}

	@Override
	public void sendUserMessage(Long chatRoomId, Long userId, String message) {
		chatMessageRepository.save(
			ChatMessage.builder()
				.chatRoomId(chatRoomId)
				.senderType(ChatMessage.SenderType.USER)
				.content(message)
				.build()
		);

		String aiReply = storyboardGeneratorPort.generate(message);

		chatMessageRepository.save(
			ChatMessage.builder()
				.chatRoomId(chatRoomId)
				.senderType(ChatMessage.SenderType.AI)
				.content(aiReply)
				.build()
		);
	}

	@Override
	public void generateStoryboard(Long chatRoomId, Long userId) {
		List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);

		if(messages.isEmpty()) {
			throw new CommonException(ErrorCode.CHAT_NOT_FOUND);
		}

		String prompt = messages.stream()
			.map(m -> m.getSenderType() + ": " + m.getContent())
			.collect(Collectors.joining("\n"));

		String result = storyboardGeneratorPort.generate(prompt);

		Storyboard storyboard = Storyboard.builder()
			.userId(userId)
			.title("스토리보드")
			.content(result)
			.build();

		storyboardRepository.save(storyboard);
	}
}
