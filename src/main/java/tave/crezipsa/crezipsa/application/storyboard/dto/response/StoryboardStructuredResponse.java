package tave.crezipsa.crezipsa.application.storyboard.dto.response;

import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;

public record StoryboardStructuredResponse(
	String cutSummary,
	String script,
	String caption,
	String time
) {

	public static StoryboardStructuredResponse from(Storyboard storyboard) {
		return new StoryboardStructuredResponse(
			storyboard.getCutSummary(),
			storyboard.getScript(),
			storyboard.getCaption(),
			storyboard.getTime()
		);
	}
}
