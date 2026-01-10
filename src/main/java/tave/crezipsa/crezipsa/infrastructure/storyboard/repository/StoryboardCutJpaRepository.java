package tave.crezipsa.crezipsa.infrastructure.storyboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tave.crezipsa.crezipsa.infrastructure.storyboard.entity.StoryboardCutJpaEntity;


public interface StoryboardCutJpaRepository  extends JpaRepository<StoryboardCutJpaEntity, Long> {

	List<StoryboardCutJpaEntity> findByStoryBoardIdOrderByCutOrderAsc(Long storyBoardId);
	@Query("select coalesce(max(c.cutOrder), 0) from StoryboardCutJpaEntity c where c.storyBoardId = :storyBoardId")
	int findMaxOrder(@Param("storyBoardId") Long storyBoardId);
}
