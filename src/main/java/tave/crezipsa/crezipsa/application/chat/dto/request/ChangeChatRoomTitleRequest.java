package tave.crezipsa.crezipsa.application.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeChatRoomTitleRequest(
	@NotBlank(message = "채팅방 제목은 비어 있을 수 없습니다.")
	String title
) {
}
