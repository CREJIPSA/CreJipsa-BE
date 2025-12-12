package tave.crezipsa.crezipsa.domain.storyboard.port;

import java.util.List;

import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;

public interface StoryboardRepositoryPort {

	Storyboard save(Storyboard storyboard);
	List<Storyboard> findByUserId(Long userId);

}
