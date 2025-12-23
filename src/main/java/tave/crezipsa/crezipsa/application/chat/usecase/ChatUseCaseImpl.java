package tave.crezipsa.crezipsa.application.chat.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatUseCaseImpl implements ChatUseCase {

	private final ChatRoomRepositoryPort chatRoomRepository;
	private final ChatMessageRepositoryPort chatMessageRepository;
	private final StoryboardGeneratorPort storyboardGeneratorPort;



	@Override
	public Long createChatRoom(Long userId, String title) {
		String checkTitle = resolveChatRooomTitle(title);
		ChatRoom room = ChatRoom.create(userId, checkTitle);
		return chatRoomRepository.save(room).getId();
	}

	@Override
	public GeminiChatResponse sendUserMessage(Long chatRoomId, Long userId, String message) {

		chatMessageRepository.save(ChatMessage.fromUser(chatRoomId,message));

		String aiReply = storyboardGeneratorPort.generate(message);

		chatMessageRepository.save(ChatMessage.fromAI(chatRoomId, aiReply));

		return new GeminiChatResponse(aiReply);
	}

	private String resolveChatRooomTitle(String title) {
		return (title == null || title.isBlank())
			? "새 채팅"
			: title;
	}

}
