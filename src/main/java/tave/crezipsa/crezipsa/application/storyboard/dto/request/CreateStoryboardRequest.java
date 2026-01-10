package tave.crezipsa.crezipsa.application.storyboard.dto.request;

public record CreateStoryboardRequest(
	String title,
	Long chatMessageId
) {
}
