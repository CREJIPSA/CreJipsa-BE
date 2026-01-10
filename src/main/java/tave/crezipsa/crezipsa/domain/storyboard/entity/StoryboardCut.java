package tave.crezipsa.crezipsa.domain.storyboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StoryboardCut {

	private final Long id;
	private final Long storyboardId;
	private final int order;
	private final String cutComposition;
	private final String script;
	private final String caption;
	private final String etc;

	public static StoryboardCut create(Long storyboardId, int order) {
		return StoryboardCut.builder()
			.storyboardId(storyboardId)
			.order(order)
			.cutComposition("")
			.script("")
			.caption("")
			.etc("")
			.build();
	}

	public StoryboardCut withContent(
		String cutComposition,
		String script,
		String caption,
		String etc
	) {
		return StoryboardCut.builder()
			.id(this.id)
			.storyboardId(this.storyboardId)
			.order(this.order)
			.cutComposition(cutComposition)
			.script(script)
			.caption(caption)
			.etc(etc)
			.build();
	}
}
