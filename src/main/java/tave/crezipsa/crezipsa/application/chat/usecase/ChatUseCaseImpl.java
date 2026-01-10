package tave.crezipsa.crezipsa.application.chat.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.global.exception.code.ErrorCode;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatDetailResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatMessageResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatRoomListResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;
import tave.crezipsa.crezipsa.domain.chat.entity.ChatRoom;
import tave.crezipsa.crezipsa.domain.chat.port.ChatMessageRepositoryPort;
import tave.crezipsa.crezipsa.domain.chat.port.ChatRoomRepositoryPort;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardGeneratorPort;
import tave.crezipsa.crezipsa.global.exception.model.CommonException;

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

	@Override
	public void changeChatRoomTitle(Long userId, Long chatRoomId, String title) {
		ChatRoom room = getChatRoom(chatRoomId, userId);
		room.changeTitle(title);
	}

	@Override
	public List<ChatRoomListResponse> getMyChatRooms(Long userId) {
		return chatRoomRepository.findByUserId(userId).stream()
			.map(room -> new ChatRoomListResponse(
				room.getId(),
				room.getTitle(),
				chatMessageRepository
					.findLastMessageAt(room.getId())
					.orElse(null)
			))
			.toList();
	}

	@Override
	public ChatMessageResponse getChatDetail(Long userId, Long chatRoomId) {
		ChatRoom room = getChatRoom(chatRoomId, userId);

		var messages =
			chatMessageRepository
				.findByChatRoomIdOrderByCreatedAtAsc(chatRoomId)
				.stream()
				.map(ChatDetailResponse::from)
				.toList();

		return new ChatMessageResponse(
			room.getId(),
			room.getTitle(),
			messages
		);
	}

	private String resolveChatRooomTitle(String title) {
		return (title == null || title.isBlank())
			? "새 채팅"
			: title;
	}

	private ChatRoom getChatRoom(Long chatRoomId, Long userId) {
		return chatRoomRepository.findByIdAndUserId(chatRoomId, userId)
			.orElseThrow(() ->
				new CommonException(ErrorCode.CHAT_ROOM_NOT_FOUND)
			);
	}

	private void validateChatRoomOwner(Long chatRoomId, Long userId) {
		if (!chatRoomRepository
			.findByIdAndUserId(chatRoomId, userId)
			.isPresent()) {
			throw new CommonException(ErrorCode.CHAT_ROOM_NOT_FOUND);
		}
	}
	private String resolveChatRoomTitle(String title) {
		return (title == null || title.isBlank())
			? "새 채팅"
			: title;
	}
}
