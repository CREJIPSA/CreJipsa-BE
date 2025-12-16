package tave.crezipsa.crezipsa.application.storyboard.dto.request;

import jakarta.validation.constraints.NotNull;

public record SaveStoryboardRequest(
	@NotNull Long chatMessageId,
	String title
) {
}
