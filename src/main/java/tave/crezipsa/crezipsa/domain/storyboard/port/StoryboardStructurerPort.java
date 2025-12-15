package tave.crezipsa.crezipsa.domain.storyboard.port;

import tave.crezipsa.crezipsa.application.chat.dto.response.StoryboardStructuredResponse;

public interface StoryboardStructurerPort {
	StoryboardStructuredResponse structure(String aiMessageContent);
}
