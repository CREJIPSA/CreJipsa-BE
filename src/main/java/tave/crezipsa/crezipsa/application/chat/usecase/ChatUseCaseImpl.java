package tave.crezipsa.crezipsa.application.chat.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardStructurerPort;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatUseCaseImpl implements ChatUseCase {

	private final ChatRoomRepositoryPort chatRoomRepository;
	private final ChatMessageRepositoryPort chatMessageRepository;
	private final StoryboardRepositoryPort storyboardRepository;
	private final StoryboardGeneratorPort storyboardGeneratorPort;
	private final StoryboardStructurerPort storyboardStructurerPort;


	@Override
	public Long createChatRoom(Long userId, String title) {
		ChatRoom room = ChatRoom.create(userId, title);
		return chatRoomRepository.save(room).getId();
	}

	@Override
	public GeminiChatResponse sendUserMessage(Long chatRoomId, Long userId, String message) {

		chatMessageRepository.save(ChatMessage.fromUser(chatRoomId,message));

		String aiReply = storyboardGeneratorPort.generate(message);

		chatMessageRepository.save(ChatMessage.fromAI(chatRoomId, aiReply));

		return new GeminiChatResponse(aiReply);
	}

	@Override
	public Long saveStoryboard(Long userId, Long chatMessageId, String title) {

		ChatMessage msg = chatMessageRepository.findById(chatMessageId);

		if (msg == null) {
			throw new CommonException(ErrorCode.CHAT_NOT_FOUND);
		}
		if (msg.getSenderType() != ChatMessage.SenderType.AI) {
			throw new CommonException(ErrorCode.INVALID_SENDER_TYPE);
		}

		StoryboardStructuredResponse structured =
			storyboardStructurerPort.structure(msg.getContent());

		Storyboard storyboard = Storyboard.create(
			userId,
			(title == null || title.isBlank()) ? "AI 스토리보드" : title,
			structured.cutSummary(),
			structured.script(),
			structured.caption(),
			structured.time()
		);

		return storyboardRepository.save(storyboard).getId();
	}
}
