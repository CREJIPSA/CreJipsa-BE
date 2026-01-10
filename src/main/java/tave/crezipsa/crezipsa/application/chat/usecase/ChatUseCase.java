package tave.crezipsa.crezipsa.application.chat.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.chat.dto.response.ChatMessageResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.ChatRoomListResponse;
import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;

public interface ChatUseCase {

	Long createChatRoom ( Long userId, String title);
	GeminiChatResponse sendUserMessage(Long chatRoomId, Long userId, String message);
	void changeChatRoomTitle(Long userId, Long chatRoomId, String title);
	List<ChatRoomListResponse> getMyChatRooms(Long userId);
	ChatMessageResponse getChatDetail(Long userId, Long chatRoomId);

}
