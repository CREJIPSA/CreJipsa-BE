package tave.crezipsa.crezipsa.infrastructure.storyboard.mapper;

import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.infrastructure.storyboard.entity.StoryboardJpaEntity;

public class StoryboardMapper {

	public static Storyboard toDomain(StoryboardJpaEntity e) {
		return Storyboard.builder()
			.id(e.getStoryBoardId())
			.userId(e.getUserId())
			.title(e.getStoryboardTitle())
			.sourceChatMessageId(e.getSourceChatMessageId())
			.createdAt(e.getCreatedAt())
			.build();
	}

	public static StoryboardJpaEntity toJpa(Storyboard d) {
		return StoryboardJpaEntity.builder()
			.storyBoardId(d.getId())
			.userId(d.getUserId())
			.storyboardTitle(d.getTitle())
			.sourceChatMessageId(d.getSourceChatMessageId())
			.createdAt(d.getCreatedAt())
			.build();
	}

}
