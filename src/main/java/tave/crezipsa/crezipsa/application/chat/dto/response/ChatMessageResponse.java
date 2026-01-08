package tave.crezipsa.crezipsa.application.chat.dto.response;

import java.util.List;

public record ChatMessageResponse(
	Long chatRoomId,
	String title,
	List<ChatDetailResponse> messages
) {
}
