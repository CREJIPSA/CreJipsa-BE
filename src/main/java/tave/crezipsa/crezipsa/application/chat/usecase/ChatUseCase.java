package tave.crezipsa.crezipsa.application.chat.usecase;

import tave.crezipsa.crezipsa.application.chat.dto.response.GeminiChatResponse;

public interface ChatUseCase {

	Long createChatRoom ( Long userId, String title);
	GeminiChatResponse sendUserMessage(Long chatRoomId, Long userId, String message);
	Long saveStoryboard(Long userId, Long chatMessageId, String title);
}
