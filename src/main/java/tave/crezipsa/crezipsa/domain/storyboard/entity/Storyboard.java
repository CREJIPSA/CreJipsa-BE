package tave.crezipsa.crezipsa.domain.storyboard.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Storyboard {

	private final Long id;
	private final Long userId;

	private final String title;

	private final String cutSummary;
	private final String script;
	private final String caption;
	private final String time;

	private final LocalDateTime createdAt;

	public static Storyboard create(Long userId, String title, String cutSummary, String script, String caption, String time) {
		return Storyboard.builder()
			.userId(userId)
			.title(title)
			.cutSummary(cutSummary)
			.script(script)
			.caption(caption)
			.time(time)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
