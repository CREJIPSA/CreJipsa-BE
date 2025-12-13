package tave.crezipsa.crezipsa.application.chat.usecase;

public interface ChatUseCase {

	Long createChatRoom ( Long userId, String title);
	void sendUserMessage(Long chatRoomId, Long userId, String message);
	void generateStoryboard(Long chatRoomId, Long userId);

}
