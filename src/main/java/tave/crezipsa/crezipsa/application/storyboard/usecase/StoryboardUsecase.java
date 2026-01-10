package tave.crezipsa.crezipsa.application.storyboard.usecase;

import java.util.List;

import tave.crezipsa.crezipsa.application.storyboard.dto.request.CreateStoryboardRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardCutRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.request.UpdateStoryboardTitleRequest;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardCutResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardEditorResponse;
import tave.crezipsa.crezipsa.application.storyboard.dto.response.StoryboardSummaryResponse;

public interface StoryboardUsecase {
	StoryboardEditorResponse create(Long userId, CreateStoryboardRequest request);

	StoryboardEditorResponse get(Long userId, Long storyboardId);

	List<StoryboardSummaryResponse> getMyList(Long userId);

	void updateTitle(Long userId, Long storyboardId, UpdateStoryboardTitleRequest request);

	StoryboardCutResponse addCut(Long userId, Long storyboardId);

	StoryboardCutResponse updateCut(Long userId, Long cutId, UpdateStoryboardCutRequest request);

	void deleteCut(Long userId, Long cutId);

	void deleteStoryboard(Long userId, Long storyboardId);
}
