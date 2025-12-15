package tave.crezipsa.crezipsa.application.chat.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
	@NotBlank String message
) { }
