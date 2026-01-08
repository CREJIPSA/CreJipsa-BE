package tave.crezipsa.crezipsa.application.chat.dto.response;

import java.time.LocalDateTime;

import tave.crezipsa.crezipsa.domain.chat.entity.ChatMessage;

public record ChatDetailResponse(
	Long messageId,
	ChatMessage.SenderType senderType,
	String content,
	LocalDateTime createdAt
) {
}
