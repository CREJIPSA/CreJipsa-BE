package tave.crezipsa.crezipsa.application.storyboard.dto.request;

public record UpdateStoryboardRequest(
	String title,
	String cutSummary,
	String script,
	String caption,
	String time
) {
}
