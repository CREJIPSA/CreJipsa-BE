package tave.crezipsa.crezipsa.application.storyboard.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardStructuredResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;

public interface StoryboardUsecase {
	StoryboardStructuredResponse createFromChatMessage(Long userId, Long chatMessageId, String title);

	StoryboardStructuredResponse get(Long userId, Long storyboardId);
	List<StoryboardSummaryResponse> getMyList(Long userId);

	StoryboardStructuredResponse update(Long userId, Long storyboardId, UpdateStoryboardRequest request);
	void delete(Long userId, Long storyboardId);
}
