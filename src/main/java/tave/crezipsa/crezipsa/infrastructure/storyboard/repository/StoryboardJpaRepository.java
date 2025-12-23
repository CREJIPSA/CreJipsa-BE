package tave.crezipsa.crezipsa.infrastructure.storyboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tave.crezipsa.crezipsa.infrastructure.storyboard.entity.StoryboardJpaEntity;

public interface StoryboardJpaRepository extends JpaRepository<StoryboardJpaEntity, Long> {
	List<StoryboardJpaEntity> findByUserId(Long userId);

}
