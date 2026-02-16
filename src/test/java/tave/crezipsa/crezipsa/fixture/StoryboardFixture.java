package tave.crezipsa.crezipsa.fixture;

import java.time.LocalDateTime;

import tave.crezipsa.crezipsa.domain.storyboard.entity.Storyboard;
import tave.crezipsa.crezipsa.domain.storyboard.entity.StoryboardCut;

public final class StoryboardFixture {

	private StoryboardFixture() {
	}

	public static Storyboard createStoryboard(Long id, Long userId) {
		return Storyboard.builder()
			.id(id)
			.userId(userId)
			.title("테스트 스토리보드")
			.sourceChatMessageId(null)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static Storyboard createStoryboard(Long id, Long userId, Long sourceChatMessageId) {
		return Storyboard.builder()
			.id(id)
			.userId(userId)
			.title("테스트 스토리보드")
			.sourceChatMessageId(sourceChatMessageId)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static StoryboardCut createStoryboardCut(Long id, Long storyboardId, int order) {
		return StoryboardCut.builder()
			.id(id)
			.storyboardId(storyboardId)
			.order(order)
			.cutComposition("컷 구도")
			.script("스크립트")
			.caption("캡션")
			.etc("기타")
			.build();
	}
}
