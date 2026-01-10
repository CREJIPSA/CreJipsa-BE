package tave.crezipsa.crezipsa.application.storyboard.dto.response;

public record StoryboardCutResponse(
	Long cutId,
	int order,
	String cutComposition,
	String script,
	String caption,
	String etc
) {
}
