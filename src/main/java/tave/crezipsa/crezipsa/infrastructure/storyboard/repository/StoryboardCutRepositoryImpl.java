package tave.crezipsa.crezipsa.infrastructure.storyboard.repository;


import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardCutRepositoryPort;
import tave.crezipsa.crezipsa.infrastructure.storyboard.mapper.StoryboardCutMapper;

@Repository
@RequiredArgsConstructor
public class StoryboardCutRepositoryImpl implements StoryboardCutRepositoryPort {

	private final StoryboardCutJpaRepository storyboardCutJpaRepository;

	@Override
	public StoryboardCut save(StoryboardCut cut) {
		return StoryboardCutMapper.toDomain(
			storyboardCutJpaRepository.save(StoryboardCutMapper.toJpa(cut))
		);
	}

	@Override
	public List<StoryboardCut> findByStoryboardId(Long storyboardId) {
		return storyboardCutJpaRepository.findByStoryBoardIdOrderByCutOrderAsc(storyboardId)
			.stream()
			.map(StoryboardCutMapper::toDomain)
			.toList();
	}

	@Override
	public StoryboardCut findById(Long cutId) {
		return storyboardCutJpaRepository.findById(cutId)
			.map(StoryboardCutMapper::toDomain)
			.orElse(null);
	}

	@Override
	public void deleteById(Long cutId) {
		storyboardCutJpaRepository.deleteById(cutId);
	}

	@Override
	public int findNextOrder(Long storyboardId) {
		int max = storyboardCutJpaRepository.findMaxOrder(storyboardId);
		return max + 1;
	}
}
