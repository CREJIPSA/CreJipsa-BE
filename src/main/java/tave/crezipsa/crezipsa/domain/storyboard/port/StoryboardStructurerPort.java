package tave.crezipsa.crezipsa.domain.storyboard.port;

import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardStructuredResponse;

public interface StoryboardStructurerPort {
	StoryboardStructuredResponse structure(String aiMessageContent);
}
