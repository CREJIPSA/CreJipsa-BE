package tave.crezipsa.crezipsa.domain.storyboard.port;

import java.util.List;

import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;

public interface StoryboardCutRepositoryPort {

	StoryboardCut save(StoryboardCut cut);
	List<StoryboardCut> findByStoryboardId(Long storyboardId);
	StoryboardCut findById(Long cutId);
	void deleteById(Long cutId);
	int findNextOrder(Long StoryboardId);
}
