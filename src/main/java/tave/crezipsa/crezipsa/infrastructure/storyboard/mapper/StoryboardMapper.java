package tave.crezipsa.crezipsa.infrastructure.storyboard.mapper;

import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.infrastructure.storyboard.entity.StoryboardJpaEntity;

public class StoryboardMapper {

	public static Storyboard toDomain(StoryboardJpaEntity e) {
		return Storyboard.builder()
			.id(e.getStoryBoardId())
			.userId(e.getUserId())
			.title(e.getStoryboardTitle())
			.cutSummary(e.getCutSummary())
			.script(e.getScript())
			.caption(e.getCaption())
			.time(e.getTime())
			.createdAt(e.getCreatedAt())
			.build();
	}

	public static StoryboardJpaEntity toJpa(Storyboard d) {
		return StoryboardJpaEntity.builder()
			.storyBoardId(d.getId())
			.userId(d.getUserId())
			.storyboardTitle(d.getTitle())
			.cutSummary(d.getCutSummary())
			.script(d.getScript())
			.caption(d.getCaption())
			.time(d.getTime())
			.createdAt(d.getCreatedAt())
			.build();
	}

}
