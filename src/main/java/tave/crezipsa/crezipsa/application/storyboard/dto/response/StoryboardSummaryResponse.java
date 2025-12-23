package tave.crezipsa.crezipsa.application.storyboard.dto.response;

import java.time.LocalDateTime;

public record StoryboardSummaryResponse(
	Long storyboardId,
	String title,
	LocalDateTime createdAt

) {
}
