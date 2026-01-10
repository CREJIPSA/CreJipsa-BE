package tave.crezipsa.crezipsa.infrastructure.storyboard.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.port.StoryboardRepositoryPort;
import tave.crezipsa.crezipsa.infrastructure.storyboard.mapper.StoryboardMapper;

@Repository
@RequiredArgsConstructor
public class StoryboardRepositoryImpl implements StoryboardRepositoryPort {

	private final StoryboardJpaRepository storyboardJpaRepository;

	@Override
	public Storyboard save(Storyboard storyboard) {
		return StoryboardMapper.toDomain(
			storyboardJpaRepository.save(StoryboardMapper.toJpa(storyboard))
		);
	}

	@Override
	public List<Storyboard> findByUserId(Long userId) {
		return storyboardJpaRepository.findByUserId(userId).stream()
			.map(StoryboardMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public Storyboard findById(Long storyboardId) {
		return storyboardJpaRepository.findById(storyboardId)
			.map(StoryboardMapper::toDomain)
			.orElse(null);
	}

	@Override
	public void deleteById(Long storyboardId) {
		storyboardJpaRepository.deleteById(storyboardId);
	}

	@Override
	public List<Storyboard> searchByTitle(Long userId, String keyword) {
		return storyboardJpaRepository
			.findByUserIdAndStoryboardTitleContaining(userId, keyword)
			.stream()
			.map(StoryboardMapper::toDomain)
			.toList();
	}
}
