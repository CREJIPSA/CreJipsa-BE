package tave.crezipsa.crezipsa.application.chat.dto.response;

import java.time.LocalDateTime;

public record ChatRoomListResponse(
	Long chatRoomId,
	String title,
	LocalDateTime lastMessageAt
) {
}
