package tave.crezipsa.crezipsa.infrastructure.storyboard.mapper;

import java.time.LocalDateTime;

import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;
import tave.crezipsa.crezipsa.infrastructure.storyboard.entity.StoryboardCutJpaEntity;

public class StoryboardCutMapper {

	public static StoryboardCut toDomain(StoryboardCutJpaEntity e) {
		return StoryboardCut.builder()
			.id(e.getCutId())
			.storyboardId(e.getStoryBoardId())
			.order(e.getCutOrder())
			.cutComposition(e.getCutComposition())
			.script(e.getScript())
			.caption(e.getCaption())
			.etc(e.getEtc())
			.build();
	}

	public static StoryboardCutJpaEntity toJpa(StoryboardCut d) {
		LocalDateTime createdAt = LocalDateTime.now();

		return StoryboardCutJpaEntity.builder()
			.cutId(d.getId())
			.storyBoardId(d.getStoryboardId())
			.cutOrder(d.getOrder())
			.cutComposition(d.getCutComposition())
			.script(d.getScript())
			.caption(d.getCaption())
			.etc(d.getEtc())
			.createdAt(createdAt)
			.build();
	}
}
