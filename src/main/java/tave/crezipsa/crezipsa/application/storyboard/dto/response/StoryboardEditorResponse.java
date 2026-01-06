package tave.crezipsa.crezipsa.application.storyboard.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record StoryboardEditorResponse(
	Long storyboardId,
	String title,
	Long sourceChatMessageId,
	LocalDateTime createdAt,
	List<StoryboardCutResponse> cuts
) {
}
